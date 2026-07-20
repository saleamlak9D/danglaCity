package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CleanPrimaryDark,
    secondary = CleanSecondaryDark,
    tertiary = CleanTertiaryDark,
    background = CleanBackgroundDark,
    surface = CleanSurfaceDark,
    onPrimary = CleanBackgroundDark,
    onSecondary = CleanBackgroundDark,
    onBackground = CleanBackgroundLight,
    onSurface = CleanBackgroundLight
)

private val LightColorScheme = lightColorScheme(
    primary = CleanPrimary,
    secondary = CleanSecondary,
    tertiary = CleanTertiary,
    background = CleanBackgroundLight,
    surface = CleanSurfaceLight,
    onPrimary = CleanSurfaceLight,
    onSecondary = CleanSurfaceLight,
    onBackground = CleanBackgroundDark,
    onSurface = CleanBackgroundDark
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Force our custom academic colors
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
