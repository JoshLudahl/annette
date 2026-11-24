package com.softklass.annette.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.material.color.DynamicColors
import com.softklass.annette.core.preferences.ThemeMode
import com.softklass.annette.core.ui.design.AnnettePageHeader
import com.softklass.annette.core.ui.design.GroupedSurface
import com.softklass.annette.core.ui.design.SectionHeader
import com.softklass.annette.core.ui.design.SegmentedToggle
import kotlinx.coroutines.launch

@Composable
fun PreferencesScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: PreferencesViewModel,
) {
    val dynamicColorEnabled by viewModel.dynamicColorEnabled.collectAsState(initial = false)
    val themeMode by viewModel.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
    ) {
        AnnettePageHeader(
            title = "Preferences",
            subtitle = "Appearance and theme",
            onBack = onNavigateBack,
        )

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(Modifier.height(10.dp))

            if (DynamicColors.isDynamicColorAvailable()) {
                SectionHeader(title = "Colour source")
                Spacer(Modifier.height(12.dp))
                GroupedSurface {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Dynamic colour",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = "Use colours from your wallpaper instead of the Annette palette.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.height(12.dp))
                        SegmentedToggle(
                            options = listOf(false, true),
                            selected = dynamicColorEnabled,
                            onSelect = { enabled ->
                                scope.launch { viewModel.setDynamicColorEnabled(enabled) }
                            },
                            label = { if (it) "Dynamic" else "Annette" },
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            SectionHeader(title = "Theme")
            Spacer(Modifier.height(12.dp))
            GroupedSurface {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Appearance",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = "Choose light, dark, or follow the system setting.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(12.dp))
                    SegmentedToggle(
                        options = ThemeMode.entries,
                        selected = themeMode,
                        onSelect = { mode -> scope.launch { viewModel.setThemeMode(mode) } },
                        label = { mode ->
                            mode.name.lowercase().replaceFirstChar { it.titlecaseChar() }
                        },
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            Text(
                text = "The Annette palette keeps the berry hero, pastel cards and chart colours " +
                    "consistent across light and dark modes.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(40.dp))
        }
    }
}
