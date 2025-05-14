package com.uzaif.ardrawing.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A blurry, glass-like card for a modern UI
 */
@Composable
fun BlurryGlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Int = 16,
    blurRadius: Int = 10,
    bgAlpha: Float = 0.3f,
    borderAlpha: Float = 0.4f,
    shadowElevation: Int = 4,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = shadowElevation.dp,
                shape = RoundedCornerShape(cornerRadius.dp),
                spotColor = Color.White.copy(alpha = 0.2f),
                ambientColor = Color.White.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(cornerRadius.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = bgAlpha)
        )
    ) {
        Box(
            modifier = Modifier
                .blur(blurRadius.dp)
                .background(Color.White.copy(alpha = bgAlpha))
                .alpha(0f)
                .fillMaxWidth()
        )
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = borderAlpha),
                    shape = RoundedCornerShape(cornerRadius.dp)
                )
                .padding(16.dp),
            content = content
        )
    }
}

/**
 * A blurry, glass-like button for a modern UI
 */
@Composable
fun BlurryGlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fontSize: TextUnit = 14.sp,
    cornerRadius: Int = 16,
    bgAlpha: Float = 0.15f,
    highlightAlpha: Float = 0.15f
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(cornerRadius.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = bgAlpha),
            disabledContainerColor = Color.Gray.copy(alpha = 0.1f)
        ),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        )
    ) {
        // Simple text directly in the button without any shadow
        Text(
            text = text.uppercase(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            letterSpacing = 0.5.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * A blurry, glass-like icon button
 */
@Composable
fun BlurryGlassIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(4.dp, CircleShape)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.3f))
            .border(1.5.dp, Color.White.copy(alpha = 0.6f), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
        content = content
    )
} 