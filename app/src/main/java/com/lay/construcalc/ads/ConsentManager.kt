package com.lay.construcalc.ads

import android.app.Activity
import android.content.Context
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

class ConsentManager(context: Context) {
    private val consentInformation =
        UserMessagingPlatform.getConsentInformation(context.applicationContext)

    fun requestConsent(activity: Activity, onComplete: () -> Unit) {
        val params = ConsentRequestParameters.Builder().build()
        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) {
                    onComplete()
                }
            },
            {
                onComplete()
            }
        )
    }

    fun canRequestAds(): Boolean = consentInformation.canRequestAds()
}
