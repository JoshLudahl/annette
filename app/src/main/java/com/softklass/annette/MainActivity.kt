package com.softklass.annette

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.softklass.annette.core.preferences.SettingsPreferences
import com.softklass.annette.core.preferences.ThemeMode
import com.softklass.annette.navigation.AnnetteApp
import com.softklass.theme.ui.theme.AnnetteTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var settingsPreferences: SettingsPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val dynamicColorEnabled by settingsPreferences.dynamicColorEnabled.collectAsState(initial = false)
            val themeMode by settingsPreferences.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
            val isDarkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
            }

            val view = LocalView.current
            SideEffect {
                val window = (view.context as? Activity)?.window ?: return@SideEffect
                WindowCompat.getInsetsController(window, view).apply {
                    // Status bar icon contrast is owned by each screen (Home draws a berry hero);
                    // the navigation bar sits on the light app background.
                    isAppearanceLightNavigationBars = !isDarkTheme
                }
            }

            AnnetteTheme(
                darkTheme = isDarkTheme,
                dynamicColorEnabled = dynamicColorEnabled,
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AnnetteApp()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnnetteAppPreview() {
    AnnetteTheme {
        AnnetteApp()
    }
}
