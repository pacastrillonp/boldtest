package co.pacastrillon.boldtest.ui.theme

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
    primary = BoldColors.ElectricBlue,
    onPrimary = Color.White,

    secondary = BoldColors.Navy,
    onSecondary = Color.White,

    tertiary = BoldColors.BoldRed,
    onTertiary = Color.White,

    background = BoldColors.Ink,
    onBackground = Color.White,

    surface = BoldColors.SurfaceDark,
    onSurface = Color.White,

    error = BoldColors.BoldRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = BoldColors.Navy,
    onPrimary = Color.White,

    secondary = BoldColors.ElectricBlue,
    onSecondary = Color.White,

    tertiary = BoldColors.BoldRed,
    onTertiary = Color.White,

    background = BoldColors.BackgroundLight,
    onBackground = BoldColors.Ink,

    surface = Color.White,
    onSurface = BoldColors.Ink,

    error = BoldColors.BoldRed,
    onError = Color.White
)

@Composable
fun BoldTestTheme(
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
