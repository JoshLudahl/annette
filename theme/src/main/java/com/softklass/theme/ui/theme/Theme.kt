package com.softklass.theme.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Immutable
data class ExtendedColorScheme(
    val bright: ColorFamily,
    val blackboard: ColorFamily,
    val liability: ColorFamily,
    val asset: ColorFamily,
    val cta: ColorFamily,
)

private val lightScheme = lightColorScheme(
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

private val darkScheme = darkColorScheme(
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

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)


val extendedLight = ExtendedColorScheme(
    bright = ColorFamily(
        brightLight,
        onBrightLight,
        brightContainerLight,
        onBrightContainerLight,
    ),
    blackboard = ColorFamily(
        blackboardLight,
        onBlackboardLight,
        blackboardContainerLight,
        onBlackboardContainerLight,
    ),
    liability = ColorFamily(
        liabilityLight,
        onLiabilityLight,
        liabilityContainerLight,
        onLiabilityContainerLight,
    ),
    asset = ColorFamily(
        assetLight,
        onAssetLight,
        assetContainerLight,
        onAssetContainerLight,
    ),
    cta = ColorFamily(
        ctaLight,
        onCtaLight,
        ctaContainerLight,
        onCtaContainerLight,
    ),
)

val extendedDark = ExtendedColorScheme(
    bright = ColorFamily(
        brightDark,
        onBrightDark,
        brightContainerDark,
        onBrightContainerDark,
    ),
    blackboard = ColorFamily(
        blackboardDark,
        onBlackboardDark,
        blackboardContainerDark,
        onBlackboardContainerDark,
    ),
    liability = ColorFamily(
        liabilityDark,
        onLiabilityDark,
        liabilityContainerDark,
        onLiabilityContainerDark,
    ),
    asset = ColorFamily(
        assetDark,
        onAssetDark,
        assetContainerDark,
        onAssetContainerDark,
    ),
    cta = ColorFamily(
        ctaDark,
        onCtaDark,
        ctaContainerDark,
        onCtaContainerDark,
    ),
)

val extendedLightMediumContrast = ExtendedColorScheme(
    bright = ColorFamily(
        brightLightMediumContrast,
        onBrightLightMediumContrast,
        brightContainerLightMediumContrast,
        onBrightContainerLightMediumContrast,
    ),
    blackboard = ColorFamily(
        blackboardLightMediumContrast,
        onBlackboardLightMediumContrast,
        blackboardContainerLightMediumContrast,
        onBlackboardContainerLightMediumContrast,
    ),
    liability = ColorFamily(
        liabilityLightMediumContrast,
        onLiabilityLightMediumContrast,
        liabilityContainerLightMediumContrast,
        onLiabilityContainerLightMediumContrast,
    ),
    asset = ColorFamily(
        assetLightMediumContrast,
        onAssetLightMediumContrast,
        assetContainerLightMediumContrast,
        onAssetContainerLightMediumContrast,
    ),
    cta = ColorFamily(
        ctaLightMediumContrast,
        onCtaLightMediumContrast,
        ctaContainerLightMediumContrast,
        onCtaContainerLightMediumContrast,
    ),
)

val extendedLightHighContrast = ExtendedColorScheme(
    bright = ColorFamily(
        brightLightHighContrast,
        onBrightLightHighContrast,
        brightContainerLightHighContrast,
        onBrightContainerLightHighContrast,
    ),
    blackboard = ColorFamily(
        blackboardLightHighContrast,
        onBlackboardLightHighContrast,
        blackboardContainerLightHighContrast,
        onBlackboardContainerLightHighContrast,
    ),
    liability = ColorFamily(
        liabilityLightHighContrast,
        onLiabilityLightHighContrast,
        liabilityContainerLightHighContrast,
        onLiabilityContainerLightHighContrast,
    ),
    asset = ColorFamily(
        assetLightHighContrast,
        onAssetLightHighContrast,
        assetContainerLightHighContrast,
        onAssetContainerLightHighContrast,
    ),
    cta = ColorFamily(
        ctaLightHighContrast,
        onCtaLightHighContrast,
        ctaContainerLightHighContrast,
        onCtaContainerLightHighContrast,
    ),
)

val extendedDarkMediumContrast = ExtendedColorScheme(
    bright = ColorFamily(
        brightDarkMediumContrast,
        onBrightDarkMediumContrast,
        brightContainerDarkMediumContrast,
        onBrightContainerDarkMediumContrast,
    ),
    blackboard = ColorFamily(
        blackboardDarkMediumContrast,
        onBlackboardDarkMediumContrast,
        blackboardContainerDarkMediumContrast,
        onBlackboardContainerDarkMediumContrast,
    ),
    liability = ColorFamily(
        liabilityDarkMediumContrast,
        onLiabilityDarkMediumContrast,
        liabilityContainerDarkMediumContrast,
        onLiabilityContainerDarkMediumContrast,
    ),
    asset = ColorFamily(
        assetDarkMediumContrast,
        onAssetDarkMediumContrast,
        assetContainerDarkMediumContrast,
        onAssetContainerDarkMediumContrast,
    ),
    cta = ColorFamily(
        ctaDarkMediumContrast,
        onCtaDarkMediumContrast,
        ctaContainerDarkMediumContrast,
        onCtaContainerDarkMediumContrast,
    ),
)

val extendedDarkHighContrast = ExtendedColorScheme(
    bright = ColorFamily(
        brightDarkHighContrast,
        onBrightDarkHighContrast,
        brightContainerDarkHighContrast,
        onBrightContainerDarkHighContrast,
    ),
    blackboard = ColorFamily(
        blackboardDarkHighContrast,
        onBlackboardDarkHighContrast,
        blackboardContainerDarkHighContrast,
        onBlackboardContainerDarkHighContrast,
    ),
    liability = ColorFamily(
        liabilityDarkHighContrast,
        onLiabilityDarkHighContrast,
        liabilityContainerDarkHighContrast,
        onLiabilityContainerDarkHighContrast,
    ),
    asset = ColorFamily(
        assetDarkHighContrast,
        onAssetDarkHighContrast,
        assetContainerDarkHighContrast,
        onAssetContainerDarkHighContrast,
    ),
    cta = ColorFamily(
        ctaDarkHighContrast,
        onCtaDarkHighContrast,
        ctaContainerDarkHighContrast,
        onCtaContainerDarkHighContrast,
    ),
)


@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun AnnetteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColorEnabled: Boolean = true,
    content:
    @Composable () -> Unit,
) {
    val useDarkTheme = darkTheme

    val colorScheme = when {
        // Disable this part to use the default colors
        dynamicColorEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        useDarkTheme -> darkScheme
        else -> lightScheme
    }

    val currentExtendedColorScheme = when {
        // You could also have dynamic extended colors if you generate them based on Material You
        // For now, using the predefined ones:
        useDarkTheme -> {
            // Here you'd select between extendedDark, extendedDarkMediumContrast, extendedDarkHighContrast
            // For simplicity, I'll pick extendedDark. You'll need to add logic for contrast.
            extendedDark // Or extendedDarkMediumContrast, extendedDarkHighContrast
        }

        else -> {
            // Similarly for light themes
            extendedLight // Or extendedLightMediumContrast, extendedLightHighContrast
        }
    }

    CompositionLocalProvider(LocalExtendedColors provides currentExtendedColorScheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content,
        )
    }
}

val LocalExtendedColors = staticCompositionLocalOf<ExtendedColorScheme> {
    error("No ExtendedColorScheme provided")
}

object ExtendedTheme {
    val colors: ExtendedColorScheme
        @Composable
        get() = LocalExtendedColors.current
}