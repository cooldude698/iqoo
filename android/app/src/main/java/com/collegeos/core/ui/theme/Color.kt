package com.collegeos.core.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ==============================================================================
// $1,000,000 Luxury Editorial Campus Palette — Premium, Ultra-Modern, Unique
// ==============================================================================

// Primary Institutional Tones (Default Branding)
val SlateBlue = Color(0xFF1E293B)       // Deep Academic Navy Primary
val SoftSteel = Color(0xFF475569)       // Secondary Slate
val MutedSlate = Color(0xFF64748B)      // Tertiary Slate

val WarmCreamLight = Color(0xFFF8FAFC)  // Clean Canvas Light Background
val SurfaceLight = Color(0xFFFFFFFF)    // Crisp Card Surface Light
val BorderLight = Color(0xFFE2E8F0)     // Subtle Border Light

// Dark Theme Variants
val DarkBackground = Color(0xFF0B0F19)  // Deep Midnight Background
val DarkSurface = Color(0xFF131C2E)     // Surface Card Dark
val DarkBorder = Color(0xFF223049)      // Dark Border

// Notice Sorter & Category Color Tokens
val CanvasBackground = Color(0xFFF8FAFC)
val SurfaceCard = Color(0xFFFFFFFF)
val SurfaceCardSecondary = Color(0xFFF1F5F9)
val BorderSubtle = Color(0xFFE2E8F0)
val TextPrimary = Color(0xFF0F172A)
val TextSecondary = Color(0xFF64748B)
val TextMuted = Color(0xFF94A3B8)

val BrandIndigo = Color(0xFF4F46E5)
val FeeEmerald = Color(0xFF10B981)
val FeeEmeraldBg = Color(0xFFECFDF5)

val ExamAmber = Color(0xFFD97706)
val ExamAmberBg = Color(0xFFFEF3C7)

val EventPurple = Color(0xFF8B5CF6)
val EventPurpleBg = Color(0xFFF3E8FF)

val CircularBlue = Color(0xFF2563EB)
val CircularBlueBg = Color(0xFFEFF6FF)

val OtherSlate = Color(0xFF475569)
val OtherSlateBg = Color(0xFFF1F5F9)

val PaletteSlateBlue = Color(0xFF5E7892)
val PaletteSoftSteel = Color(0xFFA7B7C6)
val PaletteCream = Color(0xFFF3EFDF)
val PaletteSageGreen = Color(0xFFBDCFAA)
val PaletteMossGreen = Color(0xFF8E9E83)

// Luxury Gradients ($1M Aesthetics)
val PositivePrimaryGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF4F46E5), Color(0xFF6366F1))
)
val ActionButtonGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF4F46E5), Color(0xFF3B82F6))
)
val LuxuryHeroGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF0F172A), Color(0xFF1E1B4B), Color(0xFF312E81), Color(0xFF4338CA))
)
val GlowCardGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF4F46E5), Color(0xFF7C3AED))
)
val EmeraldGlowGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF059669), Color(0xFF10B981))
)
val AmberGoldGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFD97706), Color(0xFFF59E0B))
)
val VibrantPinkVioletGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFEC4899), Color(0xFF8B5CF6))
)

// Configurable College Brand Overlay Tokens
data class CollegeBrandColors(
    val primary: Color = SlateBlue,
    val secondary: Color = SoftSteel,
    val accent: Color = Color(0xFF0EA5E9) // Accent Sky Blue
)
