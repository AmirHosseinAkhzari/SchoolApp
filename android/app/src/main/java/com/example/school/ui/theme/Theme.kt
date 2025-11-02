package com.example.school.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val DarkColorScheme = darkColorScheme(
    primary = LightSurmehDark,
    secondary = AquaDark,
    tertiary = LightBlueDark,
    background = MilkyDark,
    surface = DarkGreenDark,
    onPrimary = MilkyDark,
    onSecondary = MilkyDark,
    onTertiary = MilkyDark,
    onBackground = Milky,
    onSurface = MilkyDark
)

private val LightColorScheme = lightColorScheme(
    primary = LightSurmeh,
    secondary = Aqua,
    tertiary = LightBlue,
    background = Milky,
    surface = DarkGreen,
    onPrimary = Milky,
    onSecondary = Milky,
    onTertiary = Milky,
    onBackground = Black,
    onSurface = Milky
)

@Composable
fun SchoolTheme(
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
