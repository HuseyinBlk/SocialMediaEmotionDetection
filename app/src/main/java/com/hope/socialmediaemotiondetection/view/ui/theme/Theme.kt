package com.hope.socialmediaemotiondetection.view.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Blue40,
    onPrimary = Color.White,
    primaryContainer = Blue40Container,
    onPrimaryContainer = Color.Black,

    secondary = BlueGrey40,
    onSecondary = Color.White,
    secondaryContainer = BlueGrey40Container,
    onSecondaryContainer = Color.Black,

    tertiary = AquaBlue40,
    onTertiary = Color.White,
    tertiaryContainer = AquaBlue40Container,
    onTertiaryContainer = Color.Black,

    background = BlueGrey40,
    onBackground = Color.White,
    surface = SkyBlue40,
    onSurface = Color.White
)

fun getGradientBrush(isDarkTheme: Boolean): Brush {
    return if (isDarkTheme) {
        Brush.verticalGradient(
            colors = DarkBrushColors,
            startY = 0f,
            endY = 1000f
        )
    } else {
        Brush.verticalGradient(
            colors = LightBrushColors,
            startY = 0f,
            endY = 1000f
        )
    }
}

private val LightColorScheme = lightColorScheme(
    primary = Blue80,
    onPrimary = Color.Black,
    primaryContainer = Blue80Container,
    onPrimaryContainer = Color.White,

    secondary = BlueGrey80,
    onSecondary = Color.Black,
    secondaryContainer = BlueGrey80Container,
    onSecondaryContainer = Color.White,

    tertiary = AquaBlue80,
    onTertiary = Color.Black,
    tertiaryContainer = AquaBlue80Container,
    onTertiaryContainer = Color.White,

    background = LightCyan80,
    onBackground = Color.Black,
    surface = SkyBlue80,
    onSurface = Color.Black
)

@Composable
fun SocialMediaEmotionDetectionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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