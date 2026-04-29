package com.membership.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = AuroraRose,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF4D1729),
    onPrimaryContainer = Color(0xFFFFD8E4),
    secondary = AuroraCyan,
    onSecondary = Color(0xFF00201D),
    secondaryContainer = Color(0xFF073B3A),
    onSecondaryContainer = Color(0xFFC4FFF8),
    tertiary = AuroraAmber,
    onTertiary = Color(0xFF3A2700),
    tertiaryContainer = Color(0xFF553D00),
    onTertiaryContainer = Color(0xFFFFE4A3),
    background = InkBlack,
    onBackground = TextCream,
    surface = InkElevated,
    onSurface = TextCream,
    surfaceVariant = Color(0xFF2B2434),
    onSurfaceVariant = TextMuted,
    error = Error,
    onError = Color(0xFF601410),
    outline = Color(0xFF8B7C92)
)

private val LightColorScheme = lightColorScheme(
    primary = AuroraRose,
    onPrimary = OnPrimary,
    primaryContainer = Color(0xFFFFD8E4),
    onPrimaryContainer = Color(0xFF4A1024),
    secondary = AuroraCyan,
    onSecondary = Color(0xFF00201D),
    secondaryContainer = Color(0xFFC4FFF8),
    onSecondaryContainer = Color(0xFF003734),
    tertiary = AuroraAmber,
    onTertiary = Color(0xFF3E2E00),
    tertiaryContainer = Color(0xFFFFEDC0),
    onTertiaryContainer = Color(0xFF261A00),
    background = Color(0xFFFFF8F5),
    onBackground = Color(0xFF211722),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF211722),
    surfaceVariant = Color(0xFFF3E8F1),
    onSurfaceVariant = Color(0xFF665A67),
    error = Error,
    onError = OnPrimary,
    outline = Color(0xFF857281)
)

@Composable
fun MembershipTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
