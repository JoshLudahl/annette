package com.softklass.annette

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.softklass.annette.data.preferences.SettingsPreferences
import com.softklass.annette.navigation.AnnetteApp
import com.softklass.annette.ui.theme.AnnetteTheme
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
            AnnetteTheme(
                settingsPreferences = settingsPreferences,
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
