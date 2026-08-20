package com.equ.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EquGreen = Color(0xFF4F6F52)
private val EquGreenDark = Color(0xFF3A5240)
private val EquBackground = Color(0xFFFBF9F4)
private val EquSurface = Color(0xFFFFFFFF)

private val LightColors = lightColorScheme(
    primary = EquGreen,
    onPrimary = Color.White,
    secondary = EquGreenDark,
    background = EquBackground,
    surface = EquSurface,
)

private val DarkColors = darkColorScheme(
    primary = EquGreen,
    onPrimary = Color.White,
    secondary = EquGreenDark,
)

@Composable
fun EquTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(colorScheme = colors, content = content)
}
