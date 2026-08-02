package com.anonymous.drcconnector.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PureBlack = Color(0xFF000000)
private val DarkSurface = Color(0xFF0A0A0A)
private val DarkSurfaceVariant = Color(0xFF141414)
private val AccentRed = Color(0xFFFF3333)
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFFB0B0B0)
private val TextTertiary = Color(0xFF707070)
private val BorderColor = Color(0xFF222222)
private val SuccessGreen = Color(0xFF00E676)
private val WarningOrange = Color(0xFFFFAB40)

private val DarkColorScheme = darkColorScheme(
    primary = AccentRed,
    onPrimary = PureBlack,
    primaryContainer = Color(0x33FF3333),
    onPrimaryContainer = AccentRed,
    secondary = TextSecondary,
    onSecondary = PureBlack,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = TextPrimary,
    tertiary = TextTertiary,
    onTertiary = PureBlack,
    tertiaryContainer = DarkSurface,
    onTertiaryContainer = TextSecondary,
    background = PureBlack,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = BorderColor,
    error = AccentRed,
    onError = PureBlack,
    errorContainer = Color(0x33FF3333),
    onErrorContainer = AccentRed,
    inverseOnSurface = TextPrimary,
    inverseSurface = DarkSurfaceVariant,
    inversePrimary = AccentRed,
    surfaceTint = AccentRed,
    outlineVariant = BorderColor,
    scrim = PureBlack.copy(alpha = 0.8f)
)

@Composable
fun DrcConnectorTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
