package com.iqoo.noticesorter.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Palette from User Design Swatches (Dopely Colors)
val PaletteSlateBlue = Color(0xFF5E7892)
val PaletteSoftSteel = Color(0xFFA7B7C6)
val PaletteCream = Color(0xFFF3EFDF)
val PaletteSageGreen = Color(0xFFBDCFAA)
val PaletteMossGreen = Color(0xFF8E9E83)

// Text & Surface Neutral Tones
val PaletteDarkText = Color(0xFF2C3743)
val PaletteSubtext = Color(0xFF5C6B73)
val PaletteCardBackground = Color(0xFFFFFFFF)
val PaletteCardBorder = Color(0xFFE2E7EC)

// Gradients matching the palette swatches
val PositivePrimaryGradient = Brush.horizontalGradient(
    colors = listOf(PaletteSlateBlue, PaletteMossGreen)
)

val PositiveSageGradient = Brush.horizontalGradient(
    colors = listOf(PaletteSageGreen, PaletteMossGreen)
)

val PositiveSoftGradient = Brush.verticalGradient(
    colors = listOf(PaletteCream, Color(0xFFFFFFFF))
)

val PositiveButtonGradient = Brush.horizontalGradient(
    colors = listOf(PaletteSlateBlue, PaletteSoftSteel)
)
