package com.uzaif.ardrawing.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

/**
 * A list of color theme pairs for the animated background
 */
val backgroundThemes = listOf(
    listOf(
        Color(0xFFE50914), // Bright red main color (#E50914)
        Color(0xFFFB383F), // Slightly lighter red
        Color(0xFFFF524A), // Even lighter red/orange for gradient
        Color(0xFF8B0509)  // Darker shade of the main red
    ),
    listOf(
        Color(0xFFE84F5E), 
        Color(0xFFFCDFC5)
    ),
    listOf(
        Color(0xFFEFE9E0), 
        Color(0xFF0F9E99)
    ),
    listOf(
        Color(0xFF8ED968), 
        Color(0xFF103C1F)
    )
)

/**
 * A composable that creates a fluid-like animated gradient background
 * with a frosted glass effect overlay.
 * 
 * @param themeIndex The index of the color theme to use from backgroundThemes
 * @param animationDuration How long a single animation cycle takes in milliseconds
 * @param blurAmount The amount of blur to apply to the background (higher = more blurry)
 * @param content The content to display on top of the background
 */
@Composable
fun AnimatedGradientBackground(
    themeIndex: Int = 0,
    animationDuration: Int = 20000, // Slow animation for power efficiency
    blurAmount: Int = 20, // Blur amount for frosted glass effect
    content: @Composable BoxScope.() -> Unit
) {
    // Ensure the themeIndex is valid
    val safeThemeIndex = remember { themeIndex.coerceIn(0, backgroundThemes.size - 1) }
    val colors = remember { backgroundThemes[safeThemeIndex] }
    
    // Create infinite transitions
    val transition = rememberInfiniteTransition(label = "backgroundTransition")
    
    // First animation for the primary gradient position
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "translateAnim"
    )
    
    // Second animation for the wave effect
    val waveAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveAnim"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                // Calculate dynamic positions for fluid-like movement
                val width = size.width
                val height = size.height
                
                // Create wave-like movement
                val yOffset = sin(waveAnim) * height * 0.1f
                
                // Main gradient that moves smoothly
                val gradientOffset = Offset(
                    x = width * translateAnim,
                    y = height * 0.5f + yOffset
                )
                
                // Draw the gradient with multiple colors for a more fluid look
                drawRect(
                    brush = Brush.linearGradient(
                        colors = if (colors.size >= 3) {
                            // If we have 3+ colors, use them all for a richer effect
                            colors
                        } else {
                            // If we have just 2 colors, create a smoother gradient
                            listOf(
                                colors[0],
                                colors.getOrElse(1) { colors[0] },
                                colors[0]
                            )
                        },
                        start = Offset(0f, height * (0.5f - translateAnim * 0.2f)),
                        end = Offset(width, height * (0.5f + translateAnim * 0.2f)),
                        tileMode = TileMode.Mirror
                    )
                )
            }
    ) {
        // Add a semi-transparent blurred overlay for frosted glass effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(blurAmount.dp)
                .background(Color.Black.copy(alpha = 0.15f))
        )
        
        // Content is displayed on top of the blurred background
        content()
    }
} 