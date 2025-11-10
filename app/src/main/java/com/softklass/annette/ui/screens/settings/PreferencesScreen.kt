package com.softklass.annette.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.color.DynamicColors
import com.softklass.annette.core.preferences.ThemeMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PreferencesScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: PreferencesViewModel
) {
    val dynamicColorEnabled by viewModel.dynamicColorEnabled.collectAsState(initial = true)
    val themeMode by viewModel.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
    val scope = rememberCoroutineScope()

    // State to prevent double-clicking the back button
    var isBackButtonEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Preferences") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (isBackButtonEnabled) {
                                // Disable the button to prevent double-clicking
                                isBackButtonEnabled = false

                                // Perform the navigation
                                onNavigateBack()

                                // Re-enable the button after a delay (if needed)
                                scope.launch {
                                    delay(500) // 500ms delay
                                    isBackButtonEnabled = true
                                }
                            }
                        },
                        enabled = isBackButtonEnabled,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            Text(
                text = "Appearance",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (DynamicColors.isDynamicColorAvailable()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Dynamic Color",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = "Use colors from your wallpaper to personalize your app experience",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val options = listOf("Default", "Dynamic")

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(ToggleButtonDefaults.IconSpacing),
                    ) {
                        options.forEachIndexed { index, label ->

                            ToggleButton(
                                checked = if (dynamicColorEnabled && label == "Dynamic") {
                                    true
                                } else if (!dynamicColorEnabled && label == "Default") {
                                    true
                                } else {
                                    false
                                },
                                onCheckedChange = {
                                    scope.launch {
                                        viewModel.setDynamicColorEnabled(!dynamicColorEnabled)
                                    }
                                },
                                modifier = Modifier.weight(1f),

                                shapes =
                                    when (index) {
                                        0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                                        options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                                        else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                                    },
                            ) {
                                if (dynamicColorEnabled && label == "Dynamic" || !dynamicColorEnabled && label == "Default") {
                                    Icon(
                                        Icons.Rounded.Done,
                                        contentDescription = "Localized description",
                                    )
                                }

                                Spacer(Modifier.size(ToggleButtonDefaults.IconSpacing))

                                Text(label, maxLines = 1)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Theme Mode",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = "Choose between light, dark, or system theme",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(8.dp))

                val options = listOf(ThemeMode.LIGHT, ThemeMode.DARK, ThemeMode.SYSTEM)

                // ButtonGroup for Material 3 Expressive

                Row(
                    horizontalArrangement = Arrangement.spacedBy(ToggleButtonDefaults.IconSpacing),
                ) {
                    options.forEachIndexed { index, label ->
                        ToggleButton(
                            checked = themeMode == label,
                            onCheckedChange = {
                                scope.launch {
                                    viewModel.setThemeMode(label)
                                }
                            },
                            modifier = Modifier.weight(1f),

                            shapes =
                                when (index) {
                                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                                    options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                                },
                        ) {
                            if (themeMode == label) {
                                Icon(
                                    Icons.Rounded.Done,
                                    contentDescription = "Localized description",
                                )
                            }

                            Spacer(Modifier.size(ToggleButtonDefaults.IconSpacing))

                            Text(
                                label.name.lowercase().replaceFirstChar { it.titlecaseChar() },
                                maxLines = 1
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
