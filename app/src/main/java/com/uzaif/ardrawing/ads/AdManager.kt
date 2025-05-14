package com.uzaif.ardrawing.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.uzaif.ardrawing.ARDrawingApp

class AdManager {
    
    companion object {
        private const val TAG = "AdManager"
        private var interstitialAd: InterstitialAd? = null
        
        // Keep track of when ads were shown to avoid showing them too frequently
        private var lastAdShownTimeMs = 0L
        private const val MIN_TIME_BETWEEN_ADS_MS = 60000 // 1 minute
        
        /**
         * Preload an interstitial ad to have it ready when needed
         */
        fun preloadInterstitialAd(context: Context) {
            val adRequest = AdRequest.Builder().build()
            
            InterstitialAd.load(
                context,
                ARDrawingApp.INTERSTITIAL_AD_UNIT_ID,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.e(TAG, "Interstitial ad failed to load: ${adError.message}")
                        interstitialAd = null
                    }
                    
                    override fun onAdLoaded(ad: InterstitialAd) {
                        Log.d(TAG, "Interstitial ad loaded successfully")
                        interstitialAd = ad
                    }
                }
            )
        }
        
        /**
         * Show the interstitial ad if it's loaded and enough time has passed since the last ad
         * @param activity The activity context
         * @param onAdDismissed Called when the ad is closed (to continue app flow)
         * @return True if ad was shown, false otherwise
         */
        fun showInterstitialAd(activity: Activity, onAdDismissed: () -> Unit): Boolean {
            val currentTime = System.currentTimeMillis()
            
            // Check if enough time has passed since the last ad
            if (currentTime - lastAdShownTimeMs < MIN_TIME_BETWEEN_ADS_MS) {
                Log.d(TAG, "Not showing ad: too soon since last ad")
                onAdDismissed()
                return false
            }
            
            // Check if we have a loaded ad
            if (interstitialAd == null) {
                Log.d(TAG, "Not showing ad: No interstitial ad loaded")
                // Try to load a new one for next time
                preloadInterstitialAd(activity)
                onAdDismissed()
                return false
            }
            
            // Set fullscreen callback
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Ad was dismissed")
                    // Call the callback
                    onAdDismissed()
                    // Load the next ad
                    preloadInterstitialAd(activity)
                }
                
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e(TAG, "Ad failed to show: ${adError.message}")
                    onAdDismissed()
                    // Load the next ad
                    preloadInterstitialAd(activity)
                }
                
                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad showed fullscreen content")
                    // Ad was shown, so set to null and update last shown time
                    interstitialAd = null
                    lastAdShownTimeMs = System.currentTimeMillis()
                }
            }
            
            // Show the ad
            interstitialAd?.show(activity)
            return true
        }
    }
} 