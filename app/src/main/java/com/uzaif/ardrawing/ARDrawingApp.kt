package com.uzaif.ardrawing

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.initialization.InitializationStatus

class ARDrawingApp : Application() {
    
    companion object {
        // Ad Unit ID for interstitial ads
        const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
    }
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize the Mobile Ads SDK
        MobileAds.initialize(this) { initializationStatus: InitializationStatus ->
            // Initialization complete
        }
        
        // Optional: Set up a test device if needed for testing
        val testDeviceIds = listOf("TEST_DEVICE_ID")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)
    }
} 