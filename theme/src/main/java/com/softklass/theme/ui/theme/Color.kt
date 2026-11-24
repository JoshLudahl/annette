package com.softklass.theme.ui.theme

import androidx.compose.ui.graphics.Color

/*
 * Annette brand palette, aligned with the calmories design system.
 *
 * Raw swatches come from the provided palette strip:
 *   Berry  #5B5BF7  Primary
 *   Coral  #FF7A6B  Secondary / negative
 *   Ink    #12122B  Text
 *   Sun    #FFC94D  Warning
 *   Sky    #62C6FF  Water / info
 *   Mint   #3DDC97  Protein / success
 *   Plum   #D457C0  Activity / sugars
 *   Lilac  #A68CFF  Fiber
 *
 * The accessible "ink" variants (CoralInk, MintInk) are used for text and small icons on light
 * surfaces; the pure swatches are used for fills, charts and larger accents.
 */

// Brand swatches
val Berry = Color(0xFF5B5BF7)
val BerryDeep = Color(0xFF4440C9)
val BerrySoft = Color(0xFFECEBFE)
val Coral = Color(0xFFFF7A6B)
val CoralInk = Color(0xFFC93D32)
val CoralSoft = Color(0xFFFFEDEA)
val Sun = Color(0xFFFFC94D)
val SunDeep = Color(0xFFF0AC2A)
val Sky = Color(0xFF62C6FF)
val Mint = Color(0xFF3DDC97)
val MintInk = Color(0xFF087A53)
val Plum = Color(0xFFD457C0)
val Lilac = Color(0xFFA68CFF)

// Neutrals
val Ink = Color(0xFF12122B)
val InkSecondary = Color(0xFF5E5D78)
val ScreenLight = Color(0xFFF6F6FB)
val CardWhite = Color(0xFFFFFFFF)
val LineLight = Color(0xFFE5E4F0)
val InsightBlack = Ink
val InsightMuted = Color(0xFFB4B3C8)

// Semantic
val Positive = MintInk
val PositiveSoft = Color(0xFFE3FBF1)
val Negative = CoralInk
val NegativeSoft = CoralSoft
val Warning = Sun
val WarningDeep = SunDeep
val WarningSoft = Color(0xFFFFF6DE)

// Hero gradient: Berry lightened at the top so white display text keeps its contrast.
val HeroStart = Color(0xFF6360F6)
val HeroEnd = Color(0xFF4440C7)
val HeroMuted = Color(0xFFD9D5FF)

// Chart palette (light) — the swatches in the order the charts consume them.
val ChartBerry = Berry
val ChartSky = Sky
val ChartMint = Mint
val ChartSun = Sun
val ChartCoral = Coral
val ChartPlum = Plum
val ChartLilac = Lilac
val ChartNeutral = Color(0xFFE6E5F0)

// Material light scheme
val primaryLight = Berry
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = BerrySoft
val onPrimaryContainerLight = Color(0xFF1E1B63)
val secondaryLight = CoralInk
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = CoralSoft
val onSecondaryContainerLight = Color(0xFF7A2018)
val tertiaryLight = MintInk
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFE3FBF1)
val onTertiaryContainerLight = Color(0xFF04543A)
val errorLight = Color(0xFFD92D20)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = CoralSoft
val onErrorContainerLight = Color(0xFF7A2018)
val backgroundLight = ScreenLight
val onBackgroundLight = Ink
val surfaceLight = CardWhite
val onSurfaceLight = Ink
val surfaceVariantLight = Color(0xFFF1F0F8)
val onSurfaceVariantLight = InkSecondary
val outlineLight = Color(0xFF9C9AB2)
val outlineVariantLight = LineLight
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF201F36)
val inverseOnSurfaceLight = Color(0xFFF7F6FC)
val inversePrimaryLight = Color(0xFFC9B6FF)
val surfaceDimLight = Color(0xFFE1E0EE)
val surfaceBrightLight = CardWhite
val surfaceContainerLowestLight = CardWhite
val surfaceContainerLowLight = Color(0xFFFBFAFF)
val surfaceContainerLight = Color(0xFFF5F4FB)
val surfaceContainerHighLight = Color(0xFFEFEEF7)
val surfaceContainerHighestLight = Color(0xFFE8E6F1)

// Material dark scheme
val primaryDark = Color(0xFFB4B3FF)
val onPrimaryDark = Color(0xFF1B1760)
val primaryContainerDark = Color(0xFF2C2A70)
val onPrimaryContainerDark = Color(0xFFE5E3FF)
val secondaryDark = Color(0xFFFFA79A)
val onSecondaryDark = Color(0xFF5C1B12)
val secondaryContainerDark = Color(0xFF4D211C)
val onSecondaryContainerDark = Color(0xFFFFDAD3)
val tertiaryDark = Color(0xFF6EE7B7)
val onTertiaryDark = Color(0xFF00382A)
val tertiaryContainerDark = Color(0xFF0B4A38)
val onTertiaryContainerDark = Color(0xFFB7F5DF)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF7A271A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF0B0B14)
val onBackgroundDark = Color(0xFFF2F1FA)
val surfaceDark = Color(0xFF131322)
val onSurfaceDark = Color(0xFFF2F1FA)
val surfaceVariantDark = Color(0xFF242338)
val onSurfaceVariantDark = Color(0xFFB4B2C8)
val outlineDark = Color(0xFF8D8BA3)
val outlineVariantDark = Color(0xFF38364D)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFF2F1FA)
val inverseOnSurfaceDark = Color(0xFF1B1A2E)
val inversePrimaryDark = Berry
val surfaceDimDark = Color(0xFF0B0B14)
val surfaceBrightDark = Color(0xFF2C2A40)
val surfaceContainerLowestDark = Color(0xFF08080F)
val surfaceContainerLowDark = Color(0xFF161524)
val surfaceContainerDark = Color(0xFF1C1B2D)
val surfaceContainerHighDark = Color(0xFF262438)
val surfaceContainerHighestDark = Color(0xFF302E44)

// Extended light families
val brightLight = Sun
val onBrightLight = Ink
val brightContainerLight = WarningSoft
val onBrightContainerLight = Color(0xFF5C3D00)
val blackboardLight = Ink
val onBlackboardLight = Color(0xFFFFFFFF)
val blackboardContainerLight = Color(0xFF1B1B30)
val onBlackboardContainerLight = Color(0xFFE9E8F5)
val liabilityLight = CoralInk
val onLiabilityLight = Color(0xFFFFFFFF)
val liabilityContainerLight = CoralSoft
val onLiabilityContainerLight = Color(0xFF7A2018)
val assetLight = MintInk
val onAssetLight = Color(0xFFFFFFFF)
val assetContainerLight = Color(0xFFE3FBF1)
val onAssetContainerLight = Color(0xFF04543A)
val ctaLight = Berry
val onCtaLight = Color(0xFFFFFFFF)
val ctaContainerLight = BerrySoft
val onCtaContainerLight = Color(0xFF1E1B63)

// Extended dark families
val brightDark = Color(0xFFFFD977)
val onBrightDark = Color(0xFF3F2E00)
val brightContainerDark = Color(0xFF4A3600)
val onBrightContainerDark = Color(0xFFFFF6DE)
val blackboardDark = Color(0xFFF2F1FA)
val onBlackboardDark = Color(0xFF1B1A2E)
val blackboardContainerDark = Color(0xFF262438)
val onBlackboardContainerDark = Color(0xFFE5E3FF)
val liabilityDark = Color(0xFFFFA79A)
val onLiabilityDark = Color(0xFF450A0A)
val liabilityContainerDark = Color(0xFF5F1A1D)
val onLiabilityContainerDark = Color(0xFFFFDAD6)
val assetDark = Color(0xFF6EE7B7)
val onAssetDark = Color(0xFF00391B)
val assetContainerDark = Color(0xFF0B4A2A)
val onAssetContainerDark = Color(0xFFB7F5DF)
val ctaDark = Color(0xFFB4B3FF)
val onCtaDark = Color(0xFF1B1760)
val ctaContainerDark = Color(0xFF2C2A70)
val onCtaContainerDark = Color(0xFFE5E3FF)
