package com.softklass.annette.core.ui.design

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Sets the status bar icon brightness for the current screen.
 *
 * Pass `true` for dark icons on a light background, `false` for light icons on a saturated hero,
 * or leave it null to derive the choice from the current theme background. Every top-level screen
 * calls this (via [AnnettePageHeader] or directly) so the icons always match what is on screen.
 */
@Composable
fun StatusBarIcons(darkIcons: Boolean? = null) {
    val view = LocalView.current
    val context = LocalContext.current
    val window = remember(context) { (context as? Activity)?.window }
    val autoDarkIcons = MaterialTheme.colorScheme.background.luminance() > 0.5f
    val useDarkIcons = darkIcons ?: autoDarkIcons
    val controller = remember(window, view) { window?.let { WindowCompat.getInsetsController(it, view) } }

    SideEffect {
        controller?.isAppearanceLightStatusBars = useDarkIcons
    }
}
