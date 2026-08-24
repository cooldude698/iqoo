package com.iqoo.noticesorter.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Core Brand Colors (Modern OriginOS / Electric Indigo & Cobalt)
val BrandIndigo = Color(0xFF4F46E5)
val BrandIndigoDark = Color(0xFF3730A3)
val BrandBlue = Color(0xFF2563EB)
val BrandCyan = Color(0xFF06B6D4)
val BrandTeal = Color(0xFF0D9488)

// Neutral Canvas & Surface Tones (Clean Slate & Obsidian)
val CanvasBackground = Color(0xFFF8FAFC)
val SurfaceCard = Color(0xFFFFFFFF)
val SurfaceCardSecondary = Color(0xFFF1F5F9)
val SurfaceCardMuted = Color(0xFFE2E8F0)
val BorderSubtle = Color(0xFFE2E8F0)
val BorderActive = Color(0xFFCBD5E1)

// Typography Neutral Tones
val TextPrimary = Color(0xFF0F172A)     // Slate 900
val TextSecondary = Color(0xFF475569)   // Slate 600
val TextMuted = Color(0xFF94A3B8)       // Slate 400
val TextOnBrand = Color(0xFFFFFFFF)

// Category Accent & Container Colors
val ExamAmber = Color(0xFFD97706)
val ExamAmberBg = Color(0xFFFEF3C7)
val FeeEmerald = Color(0xFF059669)
val FeeEmeraldBg = Color(0xFFD1FAE5)
val EventPurple = Color(0xFF7C3AED)
val EventPurpleBg = Color(0xFFEDE9FE)
val CircularBlue = Color(0xFF0284C7)
val CircularBlueBg = Color(0xFFE0F2FE)
val OtherSlate = Color(0xFF475569)
val OtherSlateBg = Color(0xFFF1F5F9)

// High-tech Gradients
val PrimaryHeroGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF4338CA), Color(0xFF3B82F6), Color(0xFF06B6D4))
)

val ActionButtonGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF2563EB), Color(0xFF4F46E5))
)

val CardGlowGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFF8FAFC), Color(0xFFFFFFFF))
)

val AmberWarningGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFF59E0B), Color(0xFFD97706))
)

val EmeraldSuccessGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF10B981), Color(0xFF059669))
)

// Legacy alias compatibility if needed
val PaletteSlateBlue = BrandIndigo
val PaletteSoftSteel = TextMuted
val PaletteCream = CanvasBackground
val PaletteSageGreen = FeeEmeraldBg
val PaletteMossGreen = FeeEmerald
val PaletteDarkText = TextPrimary
val PaletteSubtext = TextSecondary
val PaletteCardBackground = SurfaceCard
val PaletteCardBorder = BorderSubtle
val PositivePrimaryGradient = PrimaryHeroGradient
val PositiveButtonGradient = ActionButtonGradient

