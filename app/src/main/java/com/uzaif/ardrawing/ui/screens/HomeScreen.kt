package com.uzaif.ardrawing.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.uzaif.ardrawing.R
import com.uzaif.ardrawing.data.BuiltInImage
import com.uzaif.ardrawing.ui.components.AnimatedGradientBackground
import com.uzaif.ardrawing.ui.components.BlurryGlassButton
import com.uzaif.ardrawing.ui.components.BlurryGlassCard
import com.uzaif.ardrawing.ui.components.BlurryGlassIconButton
import com.uzaif.ardrawing.ui.theme.AccentGreen
import com.uzaif.ardrawing.ui.theme.DeepBlue
import com.uzaif.ardrawing.ui.theme.IcyBlue
import com.uzaif.ardrawing.ui.theme.SageGreen
import com.uzaif.ardrawing.ui.theme.SoftCream
import com.uzaif.ardrawing.ui.theme.SoftGray
import com.uzaif.ardrawing.ui.theme.TerraCotta
import com.uzaif.ardrawing.viewmodel.ARDrawingViewModel
import kotlinx.coroutines.delay
import androidx.compose.foundation.verticalScroll

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: ARDrawingViewModel
) {
    val context = LocalContext.current
    
    // Dialog states for information dialogs
    var showHowItWorksDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    
    val hasStoragePermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                if (android.os.Build.VERSION.SDK_INT >= 33) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    
    // Animation state
    var isVisible by remember { mutableStateOf(false) }
    
    // Trigger animation after a small delay
    LaunchedEffect(key1 = true) {
        delay(100)
        isVisible = true
    }
    
    // Permission launcher
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasStoragePermission.value = isGranted
    }
    
    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            // Clear any previous selections
            viewModel.clearImages()
            // Add the new images
            uris.forEach { uri ->
                viewModel.addImage(uri)
            }
            // Navigate to gallery screen
            navController.navigate("gallery")
        }
    }
    
    // "How it works" Dialog
    if (showHowItWorksDialog) {
        AlertDialog(
            onDismissRequest = { showHowItWorksDialog = false },
            title = {
                Text(
                    text = "HOW AR DRAWING WORKS",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepBlue
                    )
                )
            },
            text = {
                Column {
                    Text(
                        text = "1. Select an image from your gallery or use one of our built-in images",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "2. Your image will be overlaid on the camera view",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "3. Adjust the transparency, size, and position of the image using gestures",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "4. Lock the position when you're happy with it",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "5. Create your AR masterpiece!",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showHowItWorksDialog = false }
                ) {
                    Text("GOT IT")
                }
            }
        )
    }
    
    // Terms and Permissions Dialog
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = {
                Text(
                    text = "TERMS & PERMISSIONS",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepBlue
                    )
                )
            },
            text = {
                Column {
                    Text(
                        text = "PERMISSIONS WE NEED:",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    
                    Text(
                        text = "• Camera: Required to display AR content",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "• Storage: Required to access your gallery images",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "• Internet: Required for displaying ads and online features",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Text(
                        text = "TERMS OF USE:",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    
                    Text(
                        text = "By using this app, you agree to use it responsibly and in accordance with local laws and regulations. The app includes some built-in images for your convenience.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showTermsDialog = false }
                ) {
                    Text("I UNDERSTAND")
                }
            }
        )
    }
    
    // Use the new AnimatedGradientBackground with theme 0 (purple/pink)
    AnimatedGradientBackground(themeIndex = 0) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Consistent spacing between all elements
        ) {
            // Top App Bar with Text Button Tiles
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Terms Button (Left corner)
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn() + slideInHorizontally(initialOffsetX = { -it }),
                ) {
                    BlurryGlassButton(
                        text = "TERMS",
                        onClick = { showTermsDialog = true },
                        modifier = Modifier
                            .height(48.dp)
                            .width(120.dp),
                        cornerRadius = 12
                    )
                }
                
                // Uses Button (Right corner)
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
                ) {
                    BlurryGlassButton(
                        text = "USES",
                        onClick = { showHowItWorksDialog = true },
                        modifier = Modifier
                            .height(48.dp)
                            .width(120.dp),
                        cornerRadius = 12
                    )
                }
            }
            
            // Small fixed spacer instead of flexible one
            Spacer(modifier = Modifier.height(8.dp))
            
            // Main content - now wrapped in a scrollable container
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(
                    animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
                ) + slideInVertically(
                    animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
                    initialOffsetY = { it / 2 }
                ),
                exit = fadeOut(),
                modifier = Modifier.fillMaxWidth().weight(1f) // Take full width and available height
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp), // Consistent 16dp spacing
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()) // Make scrollable
                ) {
                    // App Logo/Title - With rectangular shape
                    BlurryGlassCard(
                        modifier = Modifier
                            .fillMaxWidth(),
                        cornerRadius = 16,
                        shadowElevation = 8
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "AR DRAWING",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 28.sp,
                                        color = Color.White
                                    ),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                
                                Text(
                                    text = "CREATE BEAUTIFUL AR EXPERIENCES",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.8f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    
                    // Built-in Images Section with fixed height instead of weight
                    BlurryGlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp) // Fixed height instead of weight
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(16.dp),
                                spotColor = Color.White.copy(alpha = 0.3f)
                            ),
                        cornerRadius = 16,
                        shadowElevation = 12
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "FREE IMAGES",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                ),
                                modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
                            )
                            
                            // Scrollable row of built-in images
                            LazyRow(
                                contentPadding = PaddingValues(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                // This will be populated with your actual images from drawable
                                items(viewModel.builtInImages) { builtInImage ->
                                    BuiltInImageItem(
                                        builtInImage = builtInImage,
                                        onClick = {
                                            viewModel.selectBuiltInImage(builtInImage)
                                            navController.navigate("gallery")
                                        }
                                    )
                                }
                                
                                // If there are no built-in images yet, show placeholder text
                                if (viewModel.builtInImages.isEmpty()) {
                                    item {
                                        Text(
                                            text = "PLACE YOUR IMAGES IN RES/DRAWABLE AND UPDATE THE VIEWMODEL",
                                            modifier = Modifier.padding(16.dp),
                                            color = Color.White,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    // Buttons at the bottom
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Gallery Button - Larger rectangular tile
                        BlurryGlassButton(
                            text = "GALLERY",
                            onClick = {
                                if (hasStoragePermission.value) {
                                    // Open image picker
                                    imagePickerLauncher.launch("image/*")
                                } else {
                                    // Request permission
                                    requestPermissionLauncher.launch(
                                        if (android.os.Build.VERSION.SDK_INT >= 33) {
                                            Manifest.permission.READ_MEDIA_IMAGES
                                        } else {
                                            Manifest.permission.READ_EXTERNAL_STORAGE
                                        }
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp),
                            fontSize = 18.sp,
                            cornerRadius = 12
                        )
                        
                        // Internet Button - Larger rectangular tile
                        BlurryGlassButton(
                            text = "INTERNET",
                            onClick = {
                                // Open browser
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?tbm=isch&q=ar+drawing+reference"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp),
                            fontSize = 18.sp,
                            cornerRadius = 12
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BuiltInImageItem(
    builtInImage: BuiltInImage,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(220.dp)
            .padding(4.dp)
    ) {
        // Make images more rectangular with curved corners
        Box(
            modifier = Modifier
                .size(width = 220.dp, height = 220.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.2f))
                .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .clickable(onClick = onClick)
        ) {
            Image(
                painter = painterResource(id = builtInImage.resourceId),
                contentDescription = builtInImage.name,
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
} 