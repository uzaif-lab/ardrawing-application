package com.uzaif.ardrawing.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.uzaif.ardrawing.data.ImageData
import com.uzaif.ardrawing.ui.components.AnimatedGradientBackground
import com.uzaif.ardrawing.ui.components.BlurryGlassButton
import com.uzaif.ardrawing.ui.components.BlurryGlassCard
import com.uzaif.ardrawing.ui.theme.DeepBlue
import com.uzaif.ardrawing.ui.theme.IcyBlue
import com.uzaif.ardrawing.ui.theme.SageGreen
import com.uzaif.ardrawing.ui.theme.SoftCream
import com.uzaif.ardrawing.ui.theme.TerraCotta
import com.uzaif.ardrawing.utils.AdManager
import com.uzaif.ardrawing.utils.NetworkUtils
import com.uzaif.ardrawing.viewmodel.ARDrawingViewModel
import kotlinx.coroutines.delay
import com.uzaif.ardrawing.ads.AdManager as AdsAdManager
import androidx.activity.ComponentActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    navController: NavController,
    viewModel: ARDrawingViewModel
) {
    val context = LocalContext.current
    var animateItems by remember { mutableStateOf(false) }
    
    // Get AdManager instance
    val adManager = remember { AdManager.getInstance(context) }
    
    // Get the current activity context
    val activity = context as? ComponentActivity
    
    LaunchedEffect(key1 = true) {
        // Delay to start the animation
        delay(100)
        animateItems = true
    }
    
    // Function to navigate to camera with ad display
    val navigateToCamera = {
        if (activity != null) {
            AdsAdManager.showInterstitialAd(activity) {
                // This is called after ad is dismissed or if no ad is shown
                navController.navigate("camera") {
                    popUpTo("gallery") { inclusive = true }
                }
            }
        } else {
            // Fallback if context is not an activity
            navController.navigate("camera") {
                popUpTo("gallery") { inclusive = true }
            }
        }
    }
    
    // Use AnimatedGradientBackground instead of solid color
    AnimatedGradientBackground(themeIndex = 1) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SELECTED IMAGES",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Black.copy(alpha = 0.3f)
                    ),
                    actions = {
                        // Back button as rectangular tile
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    navigationIcon = { }
                )
            },
            containerColor = Color.Transparent // Make scaffold transparent to show background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (viewModel.selectedImages.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        BlurryGlassCard(
                            modifier = Modifier.shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(24.dp)
                            ),
                            cornerRadius = 24
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Text(
                                    text = "NO IMAGES SELECTED",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                BlurryGlassButton(
                                    text = "BACK",
                                    onClick = { navController.popBackStack() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    cornerRadius = 12
                                )
                            }
                        }
                    }
                } else {
                    // Header
                    BlurryGlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        cornerRadius = 16
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "YOUR SELECTED IMAGES",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }
                    
                    // Image Gallery - Rectangular tiles with curved corners
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .height(180.dp)
                    ) {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            itemsIndexed(viewModel.selectedImages) { index, imageData ->
                                GalleryImage(
                                    imageData = imageData,
                                    isSelected = viewModel.selectedImages.indexOf(imageData) == viewModel.currentImageIndex.value,
                                    onClick = {
                                        viewModel.currentImageIndex.value = viewModel.selectedImages.indexOf(imageData)
                                    },
                                    animateIn = animateItems,
                                    index = index,
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                    
                    // Selection indicator
                    Row(
                        modifier = Modifier
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        viewModel.selectedImages.forEachIndexed { index, _ ->
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(10.dp)
                                    .shadow(
                                        elevation = if (index == viewModel.currentImageIndex.value) 2.dp else 0.dp,
                                        shape = CircleShape
                                    )
                                    .background(
                                        color = if (index == viewModel.currentImageIndex.value)
                                            Color.White
                                        else
                                            Color.White.copy(alpha = 0.3f),
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    // Continue Button with improved text visibility
                    BlurryGlassButton(
                        text = "CONTINUE",
                        onClick = { navigateToCamera() }, // Use the wrapper function that shows ad
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(64.dp),
                        fontSize = 18.sp,
                        cornerRadius = 12
                    )
                    
                    // Show internet status if no connection available for ads
                    if (!AdManager.canLoadAds(context) && !viewModel.selectedImages.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp)
                        ) {
                            Text(
                                text = "NO INTERNET CONNECTION - ADS WILL BE SKIPPED",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.align(Alignment.Center),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GalleryImage(
    imageData: ImageData,
    isSelected: Boolean,
    onClick: () -> Unit,
    animateIn: Boolean = true,
    index: Int = 0,
    viewModel: ARDrawingViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val borderColor = if (isSelected) Color.White else Color.White.copy(alpha = 0.3f)
    val borderWidth = if (isSelected) 3.dp else 1.dp
    val elevation = if (isSelected) 12.dp else 4.dp
    
    // Animation parameters
    val delay = 100 * index // Staggered delay based on item index
    var visible by remember { mutableStateOf(!animateIn) } // Start visible if not animating
    
    // Launch animation after delay
    LaunchedEffect(animateIn) {
        if (animateIn) {
            delay(delay.toLong())
            visible = true
        }
    }
    
    // Scale and alpha animations for entry
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.6f,
        label = "scale animation"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        label = "alpha animation"
    )
    
    // Make the image tiles more rectangular with curved corners
    Box(
        modifier = Modifier
            .size(width = 160.dp, height = 180.dp)
            .scale(scale)
            .alpha(alpha)
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .background(Color.White.copy(alpha = 0.2f))
            .clickable(onClick = onClick)
    ) {
        // Check if it's a built-in image
        if (viewModel.isBuiltInImage(imageData.uri)) {
            val resourceId = viewModel.getResourceIdFromUri(imageData.uri)
            Image(
                painter = androidx.compose.ui.res.painterResource(id = resourceId),
                contentDescription = "Selected image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Image(
                painter = rememberAsyncImagePainter(model = imageData.uri),
                contentDescription = "Selected image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        if (isSelected) {
            // Selection indicator overlay with gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.3f)
                            )
                        )
                    )
            )
            
            // Selection checkmark as a small rectangular tile
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(width = 28.dp, height = 28.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(6.dp))
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White.copy(alpha = 0.6f))
                    .border(1.dp, Color.White, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✓",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
} 