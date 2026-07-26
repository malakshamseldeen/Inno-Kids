package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = CyanElectric,
    onPrimary = SpaceBackground,
    primaryContainer = NeonBlue,
    onPrimaryContainer = TextPrimary,
    secondary = CyanGlow,
    onSecondary = SpaceBackground,
    secondaryContainer = NeonPurple,
    onSecondaryContainer = TextPrimary,
    tertiary = AccentGold,
    onTertiary = SpaceBackground,
    background = SpaceBackground,
    onBackground = TextPrimary,
    surface = SpaceSurface,
    onSurface = TextPrimary,
    surfaceVariant = SpaceCardBg,
    onSurfaceVariant = TextSecondary,
    outline = SpaceGlassBorder
)

@Composable
fun InnoKidsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
