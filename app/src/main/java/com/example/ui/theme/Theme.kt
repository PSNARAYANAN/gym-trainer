package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Immersive Dark Scheme for high performance sports feel
private val IronFuelColorScheme = darkColorScheme(
    primary = IronOrange,
    onPrimary = BrandOnPrimary,
    primaryContainer = IronOrangeContainer,
    onPrimaryContainer = Color(0xFF541200),
    secondary = FuelGreen,
    onSecondary = FuelGreenOnContainer,
    secondaryContainer = FuelGreen,
    onSecondaryContainer = Color(0xFF556D00),
    background = CarbonBlack,
    onBackground = WarmOnSurface,
    surface = SteelSurface,
    onSurface = WarmOnSurface,
    surfaceVariant = SteelSurfaceHigh,
    onSurfaceVariant = WarmOnSurfaceVariant,
    outline = CharcoalOutline,
    outlineVariant = CharcoalOutline,
    error = ErrorText,
    errorContainer = ErrorContainerRed,
    inverseSurface = WarmOnSurface,
    inverseOnSurface = SteelSurfaceHigh
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    // We enforce the premium dark theme to keep the high-fidelity sport cockpit uniform across all devices
    MaterialTheme(
        colorScheme = IronFuelColorScheme,
        typography = Typography,
        content = content
    )
}
