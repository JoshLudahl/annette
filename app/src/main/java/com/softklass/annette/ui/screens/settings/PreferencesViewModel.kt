package com.softklass.annette.ui.screens.settings

import androidx.lifecycle.ViewModel
import com.softklass.annette.data.preferences.SettingsPreferences
import com.softklass.annette.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val settingsPreferences: SettingsPreferences
) : ViewModel() {
    val dynamicColorEnabled = settingsPreferences.dynamicColorEnabled

    suspend fun setDynamicColorEnabled(enabled: Boolean) {
        settingsPreferences.setDynamicColorEnabled(enabled)
    }

    val themeMode = settingsPreferences.themeMode

    suspend fun setThemeMode(mode: ThemeMode) {
        settingsPreferences.setThemeMode(mode)
    }
}
