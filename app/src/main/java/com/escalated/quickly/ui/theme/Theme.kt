package com.escalated.quickly.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = White,
    primaryContainer = OrangeLight,
    onPrimaryContainer = OrangeBurnt,
    secondary = TealAccent,
    onSecondary = White,
    secondaryContainer = TealAccent.copy(alpha = 0.2f),
    onSecondaryContainer = TealDark,
    tertiary = OrangeBurnt,
    onTertiary = White,
    background = Cream,
    onBackground = DarkText,
    surface = White,
    onSurface = DarkText,
    surfaceVariant = CreamDark,
    onSurfaceVariant = MediumText,
    outline = OrangeLight,
    outlineVariant = OrangeVeryLight,
)

@Composable
fun ThatEscalatedQuicklyTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = OrangePrimary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
