package com.uzaif.ardrawing.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Utility class to manage ads in the ARDrawing app
 */
class AdManager private constructor(private val context: Context) {
    private var mInterstitialAd: InterstitialAd? = null
    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private var isFirstLaunch: Boolean
        get() = preferences.getBoolean(KEY_FIRST_LAUNCH, true)
        set(value) = preferences.edit().putBoolean(KEY_FIRST_LAUNCH, value).apply()
    
    // Flag to track if we're currently loading an ad
    private var isLoadingAd = false
    
    // Flag to check if retrying ad load
    private val isRetrying = AtomicBoolean(false)
    private val handler = Handler(Looper.getMainLooper())
    private val retryRunnable = object : Runnable {
        override fun run() {
            if (mInterstitialAd == null && NetworkUtils.isInternetAvailable(context)) {
                Log.d(TAG, "Internet available, retrying ad load")
                loadInterstitialAd()
                isRetrying.set(false)
            } else if (!NetworkUtils.isInternetAvailable(context)) {
                // Still no internet, schedule another check
                Log.d(TAG, "Internet still not available, scheduling another retry")
                handler.postDelayed(this, RETRY_DELAY_MS)
            } else {
                // We already have an ad or we're loading one
                isRetrying.set(false)
            }
        }
    }

    /**
     * Start retrying to load ads when internet becomes available
     */
    fun startRetryingAdLoad() {
        if (isRetrying.compareAndSet(false, true)) {
            Log.d(TAG, "Starting to retry ad loading")
            handler.postDelayed(retryRunnable, RETRY_DELAY_MS)
        }
    }
    
    /**
     * Stop retrying to load ads
     */
    fun stopRetryingAdLoad() {
        Log.d(TAG, "Stopping ad load retry")
        handler.removeCallbacks(retryRunnable)
        isRetrying.set(false)
    }

    /**
     * Load the interstitial ad
     */
    fun loadInterstitialAd() {
        // Skip if we're already loading an ad
        if (isLoadingAd) {
            Log.d(TAG, "Already loading an ad, skipping this request")
            return
        }
        
        // Skip if we already have an ad loaded
        if (mInterstitialAd != null) {
            Log.d(TAG, "Ad already loaded, skipping this request")
            return
        }
        
        // Check for internet connectivity
        if (!NetworkUtils.isInternetAvailable(context)) {
            Log.d(TAG, "No internet connection available. Cannot load ad.")
            // Start retrying to load when internet becomes available
            startRetryingAdLoad()
            return
        }
        
        isLoadingAd = true
        Log.d(TAG, "Starting to load interstitial ad...")
        
        val adRequest = AdRequest.Builder().build()
        
        try {
            InterstitialAd.load(
                context,
                AD_UNIT_ID,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.e(TAG, "Ad failed to load: Code: ${adError.code}, Message: ${adError.message}, Domain: ${adError.domain}")
                        mInterstitialAd = null
                        isLoadingAd = false
                        
                        // If the failure was due to network issues, start retry mechanism
                        if (adError.code == 2) { // 2 is the error code for network errors
                            Log.d(TAG, "Network error detected, will retry loading ad")
                            startRetryingAdLoad()
                        }
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        Log.d(TAG, "Ad loaded successfully!")
                        mInterstitialAd = interstitialAd
                        isLoadingAd = false
                        // Stop retrying now that we have an ad
                        stopRetryingAdLoad()
                    }
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Exception during ad loading: ${e.message}")
            isLoadingAd = false
            mInterstitialAd = null
        }
    }

    /**
     * Show the interstitial ad and execute onAdDismissed when the ad is closed
     * Will not show ad on first app launch
     */
    fun showInterstitialAd(activity: Activity, onAdDismissed: () -> Unit, showNoInternetToast: Boolean = false) {
        if (isFirstLaunch) {
            // Don't show ad on first launch, but mark first launch as complete
            Log.d(TAG, "First launch detected, skipping ad")
            isFirstLaunch = false
            onAdDismissed()
            return
        }

        // Check for internet connectivity
        if (!NetworkUtils.isInternetAvailable(context)) {
            Log.d(TAG, "No internet connection available. Cannot show ad.")
            if (showNoInternetToast) {
                Toast.makeText(context, "No internet connection. Ad display skipped.", Toast.LENGTH_SHORT).show()
            }
            onAdDismissed()
            // Start retrying to load when internet becomes available
            startRetryingAdLoad()
            return
        }

        // Safety check for activity state
        if (activity.isFinishing || activity.isDestroyed) {
            Log.e(TAG, "Activity is finishing or destroyed, cannot show ad")
            onAdDismissed()
            return
        }

        try {
            if (mInterstitialAd != null) {
                Log.d(TAG, "Showing interstitial ad")
                mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d(TAG, "Ad was dismissed")
                        // Call the callback function when ad is dismissed
                        onAdDismissed()
                        // Load the next ad
                        loadInterstitialAd()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.e(TAG, "Ad failed to show: Code: ${adError.code}, Message: ${adError.message}")
                        onAdDismissed()
                        // Try to load a new ad for next time
                        mInterstitialAd = null
                        loadInterstitialAd()
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d(TAG, "Ad showed fullscreen content")
                        // Ad is shown, set instance to null until a new one is loaded
                        mInterstitialAd = null
                    }
                }
                mInterstitialAd?.show(activity)
            } else {
                Log.d(TAG, "The interstitial ad wasn't ready yet.")
                // Call the callback even if ad isn't ready
                onAdDismissed()
                // Try to load a new ad
                loadInterstitialAd()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception while showing ad: ${e.message}")
            onAdDismissed()
            loadInterstitialAd()
        }
    }

    companion object {
        private const val TAG = "AdManager"
        private const val PREFS_NAME = "ARDrawingPrefs"
        private const val KEY_FIRST_LAUNCH = "isFirstLaunch"
        private const val RETRY_DELAY_MS = 10000L // 10 seconds between retries
        
        // Test Ad unit ID - This is the official Google test ID for interstitial ads
        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        
        @Volatile
        private var instance: AdManager? = null
        private var isInitialized = false
        
        fun getInstance(context: Context): AdManager {
            return instance ?: synchronized(this) {
                instance ?: AdManager(context.applicationContext).also { instance = it }
            }
        }
        
        /**
         * Check if ads can potentially be loaded by testing internet connectivity
         * @param context Application context
         * @return true if ads can likely be loaded, false otherwise
         */
        fun canLoadAds(context: Context): Boolean {
            return NetworkUtils.isInternetAvailable(context)
        }
        
        /**
         * Initialize MobileAds on app startup
         */
        fun initialize(context: Context) {
            // Prevent double initialization
            if (isInitialized) {
                Log.d(TAG, "AdMob SDK already initialized, skipping")
                return
            }
            
            isInitialized = true
            Log.d(TAG, "Initializing AdMob SDK")
            
            try {
                MobileAds.initialize(context) { initializationStatus ->
                    val statusMap = initializationStatus.adapterStatusMap
                    statusMap?.forEach { (adapter, status) ->
                        Log.d(TAG, "Adapter name: $adapter, Status: ${status.initializationState}")
                    }
                    
                    // Only load ad after successful initialization
                    // Only attempt to load initial ad if internet is available
                    if (NetworkUtils.isInternetAvailable(context)) {
                        Log.d(TAG, "Internet available, loading initial ad")
                        getInstance(context).loadInterstitialAd()
                    } else {
                        Log.d(TAG, "No internet connection available during initialization. Will try to load ads later.")
                        getInstance(context).startRetryingAdLoad()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error initializing AdMob: ${e.message}")
                isInitialized = false // Allow retry if failed
            }
        }
    }
} 