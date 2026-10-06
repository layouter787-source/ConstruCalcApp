package com.lay.construcalc.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class AdManager(private val context: Context) {
    private var interstitial: InterstitialAd? = null
    private var loading = false
    private var completedActions = 0
    private var lastShownAt = 0L

    companion object {
        private const val TAG = "ConstruCalcAd"
        private const val LIVE_INTERSTITIAL = "ca-app-pub-7506276099130482/5581275875"
        private const val TEST_INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712"
        private const val MIN_INTERVAL_MS = 5 * 60 * 1000L
        private const val FREE_ACTIONS = 3
    }

    private val adUnitId: String
        get() = if (com.lay.construcalc.BuildConfig.DEBUG) TEST_INTERSTITIAL else LIVE_INTERSTITIAL

    fun initialize() {
        MobileAds.initialize(context) {
            Log.d(TAG, "AdMob initialized")
            load()
        }
    }

    fun recordCalculation() {
        completedActions++
    }

    fun maybeShow(activity: Activity, after: () -> Unit) {
        val now = System.currentTimeMillis()
        val eligible = completedActions > FREE_ACTIONS &&
            now - lastShownAt >= MIN_INTERVAL_MS
        val ad = interstitial

        if (!eligible || ad == null) {
            after()
            return
        }

        interstitial = null
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                load()
                after()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                lastShownAt = 0L
                load()
                after()
            }
        }

        lastShownAt = now
        ad.show(activity)
    }

    private fun load() {
        if (loading || interstitial != null) return
        loading = true

        InterstitialAd.load(
            context,
            adUnitId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    loading = false
                    interstitial = ad
                    Log.d(TAG, "Interstitial loaded")
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    loading = false
                    interstitial = null
                    Log.d(TAG, "Interstitial failed: ${adError.message}")
                }
            }
        )
    }
}
