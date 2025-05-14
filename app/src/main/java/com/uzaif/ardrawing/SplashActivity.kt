package com.uzaif.ardrawing

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import com.uzaif.ardrawing.utils.AdManager

/**
 * Simple splash screen activity that shows a loading message
 * and launches the main activity after initialization
 */
class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        // Initialize app components in background
        initializeAppComponents()
    }

    private fun initializeAppComponents() {
        // Use a background thread for initialization
        Thread {
            try {
                // Initialize AdMob
                AdManager.initialize(this)
                
                // Simulate loading time
                Thread.sleep(1500)
                
                // Switch to main activity on UI thread
                Handler(Looper.getMainLooper()).post {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } catch (e: Exception) {
                // Handle any initialization errors
                e.printStackTrace()
                
                // Still try to launch main activity even if there's an error
                Handler(Looper.getMainLooper()).post {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }.start()
    }
} 