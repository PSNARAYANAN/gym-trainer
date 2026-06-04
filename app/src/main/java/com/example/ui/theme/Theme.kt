package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = FuelOrange,
    secondary = IronCoreRed,
    tertiary = AmberGlow,
    background = DarkSlateBg,
    surface = DeepCarbonSurface,
    onPrimary = CleanWhite,
    onSecondary = CleanWhite,
    onBackground = CleanWhite,
    onSurface = CleanWhite
)

@Composable
fun IronFuelTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
