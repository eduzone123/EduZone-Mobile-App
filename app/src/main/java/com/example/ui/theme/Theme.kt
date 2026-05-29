package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryPurpleDark,
    secondary = SecondaryPurpleDark,
    tertiary = AccentPurple,
    background = DarkBg,
    surface = DarkSurface,
    onPrimary = DarkBg,
    onSecondary = DarkBg,
    onTertiary = DarkBg,
    onBackground = TextLight,
    onSurface = TextLight,
    outline = DarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryPurple,
    secondary = SecondaryPurple,
    tertiary = AccentPurple,
    background = LightBg,
    surface = LightSurface,
    onPrimary = LightSurface,
    onSecondary = LightSurface,
    onTertiary = LightSurface,
    onBackground = TextDark,
    onSurface = TextDark,
    outline = LilacBorder
)

@Composable
fun EduZoneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
