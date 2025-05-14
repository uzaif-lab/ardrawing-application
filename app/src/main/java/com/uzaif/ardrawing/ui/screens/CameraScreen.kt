package com.uzaif.ardrawing.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.ImageCapture
import androidx.camera.core.Camera
import androidx.camera.core.TorchState
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.uzaif.ardrawing.R
import com.uzaif.ardrawing.ui.components.AnimatedGradientBackground
import com.uzaif.ardrawing.ui.components.BlurryGlassButton
import com.uzaif.ardrawing.ui.components.BlurryGlassCard
import com.uzaif.ardrawing.ui.components.BlurryGlassIconButton
import com.uzaif.ardrawing.ui.theme.AccentGreen
import com.uzaif.ardrawing.ui.theme.AccentOrange
import com.uzaif.ardrawing.ui.theme.DeepBlue
import com.uzaif.ardrawing.ui.theme.SageGreen
import com.uzaif.ardrawing.ui.theme.SoftCream
import com.uzaif.ardrawing.ui.theme.TerraCotta
import com.uzaif.ardrawing.utils.AdManager
import com.uzaif.ardrawing.viewmodel.ARDrawingViewModel
import androidx.core.content.ContextCompat.getMainExecutor
import kotlinx.coroutines.delay

@Composable
fun CameraScreen(
    navController: NavController,
    viewModel: ARDrawingViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    // Get AdManager instance
    val adManager = remember { AdManager.getInstance(context) }
    
    val currentImage = viewModel.getCurrentImage()
    var transparency by remember { mutableStateOf(currentImage?.transparency ?: 1f) }
    
    // State for image transformation
    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    
    // State for locking image position/size
    var isLocked by remember { mutableStateOf(false) }
    
    // State for flash
    var flashState by remember { mutableStateOf(0) } // 0: off, 1: on
    
    // Reference to the camera
    var camera by remember { mutableStateOf<Camera?>(null) }
    
    // UI drawer state
    var showControlsDrawer by remember { mutableStateOf(false) }
    
    // State for the lock message
    var showLockMessage by remember { mutableStateOf(false) }
    
    // Flash button color
    val flashColor by remember(flashState) {
        mutableStateOf(
            when (flashState) {
                0 -> SageGreen.copy(alpha = 0.8f) // Green when off
                else -> TerraCotta.copy(alpha = 0.8f) // Red when on
            }
        )
    }
    
    // Camera permission state
    val hasCameraPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    
    // Permission launcher
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission.value = isGranted
    }
    
    // Request camera permission if not granted
    LaunchedEffect(key1 = true) {
        if (!hasCameraPermission.value) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
        
        // Preload the interstitial ad when the screen launches
        adManager.loadInterstitialAd()
    }
    
    // Update the transparency in the ViewModel when it changes
    LaunchedEffect(key1 = transparency) {
        viewModel.updateTransparency(transparency)
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, _, _, _ ->
                    // No actions here
                }
            }
    ) {
        // Add animated background with blur effect for theme 2
        AnimatedGradientBackground(
            themeIndex = 2,
            blurAmount = 1
        ) {
            // Empty content box for the background
        }
        
        // Camera preview
        if (hasCameraPermission.value) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    val mainExecutor = getMainExecutor(ctx)
                    
                    cameraProviderFuture.addListener({
                        try {
                            val cameraProvider = cameraProviderFuture.get()
                            
                            val preview = Preview.Builder().build()
                            preview.setSurfaceProvider(previewView.surfaceProvider)
                            
                            val imageCapture = ImageCapture.Builder()
                                .build()
                            
                            val cameraSelector = CameraSelector.Builder()
                                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                .build()
                            
                            // Unbind all use cases before rebinding
                            cameraProvider.unbindAll()
                            
                            // Bind use cases to camera and get camera instance
                            camera = cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageCapture
                            )
                        } catch (e: Exception) {
                            Log.e("CameraScreen", "Camera binding failed", e)
                        }
                    }, mainExecutor)
                    
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        }
        
        // Image overlay with gesture detection
        currentImage?.let { image ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(isLocked) {
                        if (!isLocked) {
                            detectTransformGestures { centroid, pan, zoom, rotationChange ->
                                scale *= zoom
                                rotation += rotationChange
                                offsetX += pan.x
                                offsetY += pan.y
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = if (viewModel.isBuiltInImage(image.uri)) {
                        // If it's a built-in image, load from resource
                        val resourceId = viewModel.getResourceIdFromUri(image.uri)
                        androidx.compose.ui.res.painterResource(id = resourceId)
                    } else {
                        // Otherwise, load from URI
                        rememberAsyncImagePainter(model = image.uri)
                    },
                    contentDescription = "Overlay image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(transparency)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            rotationZ = rotation,
                            translationX = offsetX,
                            translationY = offsetY
                        )
                )
            }
        }
        
        // Status text for lock state - now auto-hides
        AnimatedVisibility(
            visible = showLockMessage,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 90.dp),
                contentAlignment = Alignment.Center
            ) {
                BlurryGlassCard(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .shadow(elevation = 4.dp),
                    cornerRadius = 20,
                    blurRadius = 10
                ) {
                    Text(
                        text = "POSITION LOCKED",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
        
        // Container for top UI elements to ensure proper layering
        Box(modifier = Modifier.fillMaxWidth()) {
            // Drawer toggle button - now a rectangular tile
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 16.dp, start = 16.dp)
            ) {
                BlurryGlassButton(
                    text = if (showControlsDrawer) "HIDE" else "MENU",
                    onClick = { showControlsDrawer = !showControlsDrawer },
                    modifier = Modifier
                        .width(100.dp)
                        .height(48.dp),
                    cornerRadius = 12
                )
            }
            
            // Done button (top right) - now a rectangular tile
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp)
            ) {
                BlurryGlassButton(
                    text = "DONE",
                    onClick = {
                        // Show ad and then navigate back
                        adManager.showInterstitialAd(
                            activity = context as androidx.activity.ComponentActivity,
                            showNoInternetToast = true,
                            onAdDismissed = {
                                // Done button - Navigate back to main menu after ad is dismissed
                                navController.popBackStack(
                                    route = "home",
                                    inclusive = false
                                )
                            }
                        )
                    },
                    modifier = Modifier
                        .width(100.dp)
                        .height(48.dp),
                    cornerRadius = 12
                )
            }
        }
        
        // Controls drawer - horizontal at the top but below the buttons
        AnimatedVisibility(
            visible = showControlsDrawer,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp) // Position it below the toggle button
        ) {
            BlurryGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                cornerRadius = 16
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Flash toggle as rectangular tile
                    GlassControlButton(
                        text = "FLASH",
                        onClick = {
                            flashState = (flashState + 1) % 2
                            camera?.let {
                                if (it.cameraInfo.hasFlashUnit()) {
                                    it.cameraControl.enableTorch(flashState == 1)
                                }
                            }
                        }
                    )
                    
                    // Lock toggle as rectangular tile
                    GlassControlButton(
                        text = if (isLocked) "UNLOCK" else "LOCK",
                        onClick = { 
                            isLocked = !isLocked
                        }
                    )
                    
                    // Home button as rectangular tile
                    GlassControlButton(
                        text = "HOME",
                        onClick = {
                            navController.popBackStack(
                                route = "home",
                                inclusive = false
                            )
                        }
                    )
                }
            }
        }
        
        // Transparency slider at the bottom with frosted glass styling
        BlurryGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                .align(Alignment.BottomCenter),
            cornerRadius = 16
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "TRANSPARENCY",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Slider(
                    value = transparency,
                    onValueChange = { newValue -> 
                        transparency = newValue
                    },
                    valueRange = 0.05f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = SoftCream,
                        activeTrackColor = TerraCotta,
                        inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
fun GlassControlButton(
    text: String,
    onClick: () -> Unit
) {
    // Use a wider width for buttons that need more space to prevent wrapping
    val buttonWidth = when {
        text == "HOME" || text == "UNLOCK" -> 110.dp
        else -> 90.dp
    }
    val buttonHeight = 48.dp
    
    BlurryGlassButton(
        text = text,
        onClick = onClick,
        modifier = Modifier
            .width(buttonWidth)
            .height(buttonHeight),
        bgAlpha = 0.15f,  // More transparent glass effect
        cornerRadius = 12,
        fontSize = 12.sp  // Even smaller text size to prevent wrapping
    )
} 