package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = TerracottaPrimary,
    onPrimary = Color.White,
    primaryContainer = TerracottaContainer,
    onPrimaryContainer = OnTerracottaContainer,
    secondary = BasilGreenVeg,
    onSecondary = Color.White,
    secondaryContainer = BasilGreenContainer,
    onSecondaryContainer = Color(0xFF0F3812),
    tertiary = AmberAccent,
    onTertiary = Color.White,
    tertiaryContainer = AmberContainer,
    onTertiaryContainer = Color(0xFF451A03),
    background = WarmBackground,
    onBackground = TextPrimary,
    surface = WarmSurface,
    onSurface = TextPrimary,
    surfaceVariant = WarmSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = WarmOutline
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF8A65),
    onPrimary = Color(0xFF4E1402),
    primaryContainer = Color(0xFF7A250B),
    onPrimaryContainer = Color(0xFFFFDBCF),
    secondary = Color(0xFF81C784),
    onSecondary = Color(0xFF00390F),
    secondaryContainer = Color(0xFF1B5E20),
    onSecondaryContainer = Color(0xFFA5D6A7),
    background = Color(0xFF181513),
    onBackground = Color(0xFFEDE0D8),
    surface = Color(0xFF221E1B),
    onSurface = Color(0xFFEDE0D8),
    surfaceVariant = Color(0xFF2E2925),
    onSurfaceVariant = Color(0xFFC7BDB5),
    outline = Color(0xFF544A42)
)

@Composable
fun MyApplicationTheme(
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
