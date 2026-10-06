package com.lay.construcalc.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class AdManager(context: Context) {
    private val appContext = context.applicationContext
    private val prefs = appContext.getSharedPreferences("construcalc_ads", Context.MODE_PRIVATE)
    private var interstitial: InterstitialAd? = null
    private var loading = false
    private var loadedAt = 0L

    companion object {
        private const val TAG = "ConstruCalcAd"
        private const val LIVE_INTERSTITIAL = "ca-app-pub-7506276099130482/5581275875"
        private const val TEST_INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712"
        private const val MIN_INTERVAL_MS = 5 * 60 * 1000L
        private const val AD_MAX_AGE_MS = 55 * 60 * 1000L
        private const val FREE_ACTIONS = 3
        private const val ACTIONS_KEY = "completed_actions"
        private const val LAST_SHOWN_KEY = "last_shown_at"
    }

    private val adUnitId: String
        get() = if (com.lay.construcalc.BuildConfig.DEBUG) TEST_INTERSTITIAL else LIVE_INTERSTITIAL

    fun initialize() {
        MobileAds.initialize(appContext) {
            Log.d(TAG, "AdMob initialized")
            load()
        }
    }

    fun recordCalculation() {
        val count = prefs.getInt(ACTIONS_KEY, 0) + 1
        prefs.edit().putInt(ACTIONS_KEY, count).apply()
    }

    fun maybeShow(activity: Activity, after: () -> Unit) {
        val now = System.currentTimeMillis()
        val completedActions = prefs.getInt(ACTIONS_KEY, 0)
        val lastShownAt = prefs.getLong(LAST_SHOWN_KEY, 0L)

        if (completedActions <= FREE_ACTIONS || now - lastShownAt < MIN_INTERVAL_MS) {
            after()
            return
        }

        if (interstitial == null || now - loadedAt > AD_MAX_AGE_MS) {
            interstitial = null
            load()
            after()
            return
        }

        val ad = interstitial ?: run {
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
                prefs.edit().putLong(LAST_SHOWN_KEY, 0L).apply()
                load()
                after()
            }
        }

        prefs.edit().putLong(LAST_SHOWN_KEY, now).apply()
        ad.show(activity)
    }

    private fun load() {
        if (loading || interstitial != null) return
        loading = true

        InterstitialAd.load(
            appContext,
            adUnitId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    loading = false
                    interstitial = ad
                    loadedAt = System.currentTimeMillis()
                    Log.d(TAG, "Interstitial loaded")
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    loading = false
                    interstitial = null
                    Log.d(TAG, "Interstitial failed: " + adError.message)
                }
            }
        )
    }
}
