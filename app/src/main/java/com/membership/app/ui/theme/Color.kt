package com.membership.app.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// 主色调：玫瑰金 → 珊瑚粉
val PrimaryLight = Color(0xFFFF8A80)
val Primary = Color(0xFFE91E63)
val PrimaryDark = Color(0xFFAD1457)
val PrimaryContainer = Color(0xFFFCE4EC)
val OnPrimaryContainer = Color(0xFF880E4F)

// 次要色调：薰衣草紫
val SecondaryLight = Color(0xFFCE93D8)
val Secondary = Color(0xFF9C27B0)
val SecondaryDark = Color(0xFF7B1FA2)
val SecondaryContainer = Color(0xFFF3E5F5)
val OnSecondaryContainer = Color(0xFF4A148C)

// 第三色：暖金
val TertiaryLight = Color(0xFFFFD54F)
val Tertiary = Color(0xFFFFB300)
val TertiaryDark = Color(0xFFFF8F00)
val TertiaryContainer = Color(0xFFFFF8E1)

// 背景色
val BackgroundLight = Color(0xFFFFFBFE)
val BackgroundDark = Color(0xFF1C1B1F)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceDark = Color(0xFF2B2930)
val SurfaceVariantLight = Color(0xFFF3EDF7)
val SurfaceVariantDark = Color(0xFF49454F)

// 文字颜色
val OnPrimary = Color(0xFFFFFFFF)
val OnSecondary = Color(0xFFFFFFFF)
val OnBackgroundLight = Color(0xFF1C1B1F)
val OnBackgroundDark = Color(0xFFE6E1E5)
val OnSurfaceLight = Color(0xFF1C1B1F)
val OnSurfaceDark = Color(0xFFE6E1E5)
val OnSurfaceVariantLight = Color(0xFF49454F)
val OnSurfaceVariantDark = Color(0xFFCAC4D0)

// 功能色
val Success = Color(0xFF4CAF50)
val Error = Color(0xFFEF5350)
val Warning = Color(0xFFFF9800)
val Info = Color(0xFF42A5F5)

// 现代动效 UI：深色玻璃、玫瑰、青色、暖金
val InkBlack = Color(0xFF0D0B12)
val InkElevated = Color(0xFF17131F)
val SurfaceGlow = Color(0xCC211A2A)
val AuroraRose = Color(0xFFFF4F7B)
val AuroraCyan = Color(0xFF21D4C3)
val AuroraAmber = Color(0xFFFFC857)
val TextCream = Color(0xFFFFF6EA)
val TextMuted = Color(0xFFD6CBDC)

// 渐变色 Brush
val GradientRoseGold = Brush.linearGradient(
    colors = listOf(Color(0xFFE91E63), Color(0xFFFF6F00), Color(0xFFFFB300))
)

val GradientLavender = Brush.linearGradient(
    colors = listOf(Color(0xFF9C27B0), Color(0xFF7C4DFF), Color(0xFF448AFF))
)

val GradientSunset = Brush.linearGradient(
    colors = listOf(Color(0xFFFF6F00), Color(0xFFE91E63), Color(0xFF9C27B0))
)

val GradientOcean = Brush.linearGradient(
    colors = listOf(Color(0xFF42A5F5), Color(0xFF7C4DFF), Color(0xFFE040FB))
)

val GradientMint = Brush.linearGradient(
    colors = listOf(Color(0xFF00BFA5), Color(0xFF64FFDA), Color(0xFFB2FF59))
)

// 海报模板专用渐变
val PosterGradientRose = Brush.verticalGradient(
    colors = listOf(Color(0xFFFF6B6B), Color(0xFFEE5A24), Color(0xFFF0932B))
)

val PosterGradientMinimal = Brush.verticalGradient(
    colors = listOf(Color(0xFF2D3436), Color(0xFF636E72), Color(0xFFB2BEC3))
)

val PosterGradientDream = Brush.verticalGradient(
    colors = listOf(Color(0xFF6C5CE7), Color(0xFFA29BFE), Color(0xFFFD79A8))
)
