package com.uzaif.ardrawing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uzaif.ardrawing.ui.screens.CameraScreen
import com.uzaif.ardrawing.ui.screens.GalleryScreen
import com.uzaif.ardrawing.ui.screens.HomeScreen
import com.uzaif.ardrawing.ui.theme.ARDrawingTheme
import com.uzaif.ardrawing.utils.AdManager
import com.uzaif.ardrawing.utils.BuiltInImagesManager
import com.uzaif.ardrawing.viewmodel.ARDrawingViewModel
import com.uzaif.ardrawing.ads.AdManager as AdsAdManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Preload interstitial ad
        AdsAdManager.preloadInterstitialAd(this)
        
        // AdMob is already initialized in SplashActivity
        // No need to initialize again
        
        enableEdgeToEdge()
        setContent {
            ARDrawingTheme {
                val navController = rememberNavController()
                val viewModel: ARDrawingViewModel = viewModel()
                
                // Register built-in images
                BuiltInImagesManager.registerBuiltInImages { images ->
                    // Update the ViewModel with built-in images
                    viewModel.updateBuiltInImages(images)
                }
                
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ARDrawingNavHost(
                        navController = navController,
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ARDrawingNavHost(
    navController: NavHostController,
    viewModel: ARDrawingViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        
        composable("gallery") {
            GalleryScreen(navController = navController, viewModel = viewModel)
        }
        
        composable("camera") {
            CameraScreen(navController = navController, viewModel = viewModel)
        }
    }
}