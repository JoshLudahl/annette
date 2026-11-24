package com.softklass.theme.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
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
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

/**
 * Extra semantic colours used by Annette's custom chrome, beyond the Material roles.
 *
 * Material still owns surfaces, text and primary actions. These colours cover the pieces the
 * reference design relies on: the berry hero, the ink insight banner, warning states and the
 * categorical chart palette.
 */
@Immutable
data class BrandColors(
    val heroStart: Color,
    val heroEnd: Color,
    val onHero: Color,
    val heroGlow: Color,
    val heroMuted: Color,
    val insightBackground: Color,
    val onInsight: Color,
    val insightMuted: Color,
    val positive: Color,
    val positiveSoft: Color,
    val negative: Color,
    val negativeSoft: Color,
    val warning: Color,
    val warningEnd: Color,
    val onWarning: Color,
    val warningSoft: Color,
    val berry: Color,
    val berrySoft: Color,
    val chart: ChartPalette,
)

@Immutable
data class ChartPalette(
    val berry: Color,
    val sky: Color,
    val mint: Color,
    val sun: Color,
    val coral: Color,
    val plum: Color,
    val lilac: Color,
    val neutral: Color,
) {
    val all: List<Color> = listOf(berry, sky, mint, sun, coral, plum, lilac)
}

private val lightChartPalette = ChartPalette(
    berry = ChartBerry,
    sky = ChartSky,
    mint = ChartMint,
    sun = ChartSun,
    coral = ChartCoral,
    plum = ChartPlum,
    lilac = ChartLilac,
    neutral = ChartNeutral,
)

private val darkChartPalette = ChartPalette(
    berry = Color(0xFFB4B3FF),
    sky = Color(0xFF8ED7FF),
    mint = Color(0xFF6EE7B7),
    sun = Color(0xFFFFD977),
    coral = Color(0xFFFFA79A),
    plum = Color(0xFFE68CD6),
    lilac = Color(0xFFC9B6FF),
    neutral = Color(0xFF2B2A3D),
)

val lightBrandColors = BrandColors(
    heroStart = HeroStart,
    heroEnd = HeroEnd,
    onHero = Color.White,
    heroGlow = Color(0x26FFFFFF),
    heroMuted = HeroMuted,
    insightBackground = InsightBlack,
    onInsight = Color.White,
    insightMuted = InsightMuted,
    positive = Positive,
    positiveSoft = PositiveSoft,
    negative = Negative,
    negativeSoft = NegativeSoft,
    warning = Warning,
    warningEnd = WarningDeep,
    onWarning = Ink,
    warningSoft = WarningSoft,
    berry = Berry,
    berrySoft = BerrySoft,
    chart = lightChartPalette,
)

val darkBrandColors = BrandColors(
    heroStart = Color(0xFF4A46D6),
    heroEnd = Color(0xFF25216E),
    onHero = Color.White,
    heroGlow = Color(0x2EFFFFFF),
    heroMuted = Color(0xFFC9C4FF),
    insightBackground = Color(0xFFF2F1FA),
    onInsight = Ink,
    insightMuted = Color(0xFF5E5D78),
    positive = Color(0xFF6EE7B7),
    positiveSoft = Color(0xFF0B3B2A),
    negative = Color(0xFFFFA79A),
    negativeSoft = Color(0xFF40161A),
    warning = Color(0xFFFFD977),
    warningEnd = Color(0xFFF0B84A),
    onWarning = Color(0xFF2A1208),
    warningSoft = Color(0xFF3B2A0D),
    berry = Color(0xFFB4B3FF),
    berrySoft = Color(0xFF2C2A70),
    chart = darkChartPalette,
)

val LocalBrandColors = staticCompositionLocalOf { lightBrandColors }

object BrandTheme {
    val colors: BrandColors
        @Composable
        get() = LocalBrandColors.current
}

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color,
)

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

val extendedLight = ExtendedColorScheme(
    bright = ColorFamily(brightLight, onBrightLight, brightContainerLight, onBrightContainerLight),
    blackboard = ColorFamily(blackboardLight, onBlackboardLight, blackboardContainerLight, onBlackboardContainerLight),
    liability = ColorFamily(liabilityLight, onLiabilityLight, liabilityContainerLight, onLiabilityContainerLight),
    asset = ColorFamily(assetLight, onAssetLight, assetContainerLight, onAssetContainerLight),
    cta = ColorFamily(ctaLight, onCtaLight, ctaContainerLight, onCtaContainerLight),
)

val extendedDark = ExtendedColorScheme(
    bright = ColorFamily(brightDark, onBrightDark, brightContainerDark, onBrightContainerDark),
    blackboard = ColorFamily(blackboardDark, onBlackboardDark, blackboardContainerDark, onBlackboardContainerDark),
    liability = ColorFamily(liabilityDark, onLiabilityDark, liabilityContainerDark, onLiabilityContainerDark),
    asset = ColorFamily(assetDark, onAssetDark, assetContainerDark, onAssetContainerDark),
    cta = ColorFamily(ctaDark, onCtaDark, ctaContainerDark, onCtaContainerDark),
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

/**
 * Rounded, friendly shape scale. Cards use [Shapes.large] / [Shapes.extraLarge], pills and icon
 * tiles use circles or the smaller sizes.
 */
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

@Composable
fun AnnetteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic colour is opt-in; the Annette palette is the default so the brand stays consistent.
    dynamicColorEnabled: Boolean = false,
    content: @Composable () -> Unit,
) {
    val useDarkTheme = darkTheme

    val colorScheme = when {
        dynamicColorEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        useDarkTheme -> darkScheme
        else -> lightScheme
    }

    val currentExtendedColorScheme = if (useDarkTheme) extendedDark else extendedLight
    val currentBrandColors = if (useDarkTheme) darkBrandColors else lightBrandColors

    CompositionLocalProvider(
        LocalExtendedColors provides currentExtendedColorScheme,
        LocalBrandColors provides currentBrandColors,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            content = content,
        )
    }
}

val LocalExtendedColors = staticCompositionLocalOf<ExtendedColorScheme> {
    extendedLight
}

object ExtendedTheme {
    val colors: ExtendedColorScheme
        @Composable
        get() = LocalExtendedColors.current
}
