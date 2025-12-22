package com.rach.texttospeechbyvishlabs.ui.theme

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.vislab.texttospeech.theme.backgroundDark
import com.vislab.texttospeech.theme.backgroundLight
import com.vislab.texttospeech.theme.errorContainerDark
import com.vislab.texttospeech.theme.errorContainerLight
import com.vislab.texttospeech.theme.errorDark
import com.vislab.texttospeech.theme.errorLight
import com.vislab.texttospeech.theme.inverseOnSurfaceDark
import com.vislab.texttospeech.theme.inverseOnSurfaceLight
import com.vislab.texttospeech.theme.inversePrimaryDark
import com.vislab.texttospeech.theme.inversePrimaryLight
import com.vislab.texttospeech.theme.inverseSurfaceDark
import com.vislab.texttospeech.theme.inverseSurfaceLight
import com.vislab.texttospeech.theme.onBackgroundDark
import com.vislab.texttospeech.theme.onBackgroundLight
import com.vislab.texttospeech.theme.onErrorContainerDark
import com.vislab.texttospeech.theme.onErrorContainerLight
import com.vislab.texttospeech.theme.onErrorDark
import com.vislab.texttospeech.theme.onErrorLight
import com.vislab.texttospeech.theme.onPrimaryContainerDark
import com.vislab.texttospeech.theme.onPrimaryContainerLight
import com.vislab.texttospeech.theme.onPrimaryDark
import com.vislab.texttospeech.theme.onPrimaryLight
import com.vislab.texttospeech.theme.onSecondaryContainerDark
import com.vislab.texttospeech.theme.onSecondaryContainerLight
import com.vislab.texttospeech.theme.onSecondaryDark
import com.vislab.texttospeech.theme.onSecondaryLight
import com.vislab.texttospeech.theme.onSurfaceDark
import com.vislab.texttospeech.theme.onSurfaceLight
import com.vislab.texttospeech.theme.onSurfaceVariantDark
import com.vislab.texttospeech.theme.onSurfaceVariantLight
import com.vislab.texttospeech.theme.onTertiaryContainerDark
import com.vislab.texttospeech.theme.onTertiaryContainerLight
import com.vislab.texttospeech.theme.onTertiaryDark
import com.vislab.texttospeech.theme.onTertiaryLight
import com.vislab.texttospeech.theme.outlineDark
import com.vislab.texttospeech.theme.outlineLight
import com.vislab.texttospeech.theme.outlineVariantDark
import com.vislab.texttospeech.theme.outlineVariantLight
import com.vislab.texttospeech.theme.primaryContainerDark
import com.vislab.texttospeech.theme.primaryContainerLight
import com.vislab.texttospeech.theme.primaryDark
import com.vislab.texttospeech.theme.primaryLight
import com.vislab.texttospeech.theme.scrimDark
import com.vislab.texttospeech.theme.scrimLight
import com.vislab.texttospeech.theme.secondaryContainerDark
import com.vislab.texttospeech.theme.secondaryContainerLight
import com.vislab.texttospeech.theme.secondaryDark
import com.vislab.texttospeech.theme.secondaryLight
import com.vislab.texttospeech.theme.surfaceBrightDark
import com.vislab.texttospeech.theme.surfaceBrightLight
import com.vislab.texttospeech.theme.surfaceContainerDark
import com.vislab.texttospeech.theme.surfaceContainerHighDark
import com.vislab.texttospeech.theme.surfaceContainerHighLight
import com.vislab.texttospeech.theme.surfaceContainerHighestDark
import com.vislab.texttospeech.theme.surfaceContainerHighestLight
import com.vislab.texttospeech.theme.surfaceContainerLight
import com.vislab.texttospeech.theme.surfaceContainerLowDark
import com.vislab.texttospeech.theme.surfaceContainerLowLight
import com.vislab.texttospeech.theme.surfaceContainerLowestDark
import com.vislab.texttospeech.theme.surfaceContainerLowestLight
import com.vislab.texttospeech.theme.surfaceDark
import com.vislab.texttospeech.theme.surfaceDimDark
import com.vislab.texttospeech.theme.surfaceDimLight
import com.vislab.texttospeech.theme.surfaceLight
import com.vislab.texttospeech.theme.surfaceVariantDark
import com.vislab.texttospeech.theme.surfaceVariantLight
import com.vislab.texttospeech.theme.tertiaryContainerDark
import com.vislab.texttospeech.theme.tertiaryContainerLight
import com.vislab.texttospeech.theme.tertiaryDark
import com.vislab.texttospeech.theme.tertiaryLight

private val LightColorScheme2 = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val DarkColorScheme2 = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

@Composable
fun HabitChangeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme2
        else -> LightColorScheme2
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            setUpEdgeToEdge(view, darkTheme)
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

private fun setUpEdgeToEdge(view: View, darkTheme: Boolean) {
    val window = (view.context as Activity).window
    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.statusBarColor = Color.Transparent.toArgb()
    val navigationBarColor = when {
        Build.VERSION.SDK_INT >= 29 -> Color.Transparent.toArgb()
        Build.VERSION.SDK_INT >= 26 -> Color(0xFF, 0xFF, 0xFF, 0x63).toArgb()
        // Min sdk version for this app is 24, this block is for SDK versions 24 and 25
        else -> Color(0x00, 0x00, 0x00, 0x50).toArgb()
    }
    window.navigationBarColor = navigationBarColor
    val controller = WindowCompat.getInsetsController(window, view)
    controller.isAppearanceLightStatusBars = !darkTheme
    controller.isAppearanceLightNavigationBars = !darkTheme
}