package com.uzaif.ardrawing.ui.theme

import androidx.compose.ui.graphics.Color

// Artist-inspired color palette
val DeepBlue = Color(0xFF304F6D)      // Deep blue for primary
val SageGreen = Color(0xFF899481)     // Sage green for secondary
val TerraCotta = Color(0xFFE07D54)    // Terra cotta for accents and highlights
val SoftCream = Color(0xFFFFE1A0)     // Soft cream for warm accents
val IcyBlue = Color(0xFFE2F3FD)       // Light icy blue for backgrounds
val SoftGray = Color(0xFFE6E1DD)      // Soft gray for surfaces

// Light Theme Colors
val PrimaryLight = DeepBlue
val SecondaryLight = SageGreen
val TertiaryLight = TerraCotta
val BackgroundLight = IcyBlue
val SurfaceLight = Color.White
val OnPrimaryLight = Color.White
val OnBackgroundLight = DeepBlue.copy(alpha = 0.87f)

// Dark Theme Colors
val PrimaryDark = DeepBlue.copy(red = 0.2f, green = 0.2f, blue = 0.3f)
val SecondaryDark = SageGreen.copy(red = 0.15f, green = 0.2f, blue = 0.15f)
val TertiaryDark = TerraCotta
val BackgroundDark = Color(0xFF121212)
val SurfaceDark = Color(0xFF202020)
val OnPrimaryDark = Color.White
val OnBackgroundDark = Color.White.copy(alpha = 0.87f)

// Accent colors used throughout the app
val AccentOrange = TerraCotta
val AccentCream = SoftCream
val AccentGreen = SageGreen