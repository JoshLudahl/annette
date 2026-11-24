package com.softklass.annette.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.softklass.annette.core.ui.design.AnnettePageHeader
import com.softklass.annette.core.ui.design.GroupedSurface
import com.softklass.annette.core.ui.design.PastelIconTile
import com.softklass.annette.core.ui.design.SectionHeader
import com.softklass.theme.ui.theme.AnnetteTheme
import com.softklass.theme.ui.theme.BrandTheme

data class SettingsItem(
    val title: String,
    val subtitle: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val onClick: () -> Unit = {},
)

@Composable
fun SettingsScreen(
    onSettingsItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val settingsItems = listOf(
        SettingsItem(
            title = "Preferences",
            subtitle = "Theme, dynamic colour and appearance",
            icon = Icons.Rounded.DarkMode,
            onClick = onSettingsItemClick,
        ),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
    ) {
        AnnettePageHeader(
            title = "Settings",
            subtitle = "Make Annette yours",
        )

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(Modifier.height(10.dp))

            GroupedSurface {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PastelIconTile(
                        icon = Icons.Rounded.Person,
                        tint = BrandTheme.colors.berry,
                        container = BrandTheme.colors.berrySoft,
                        size = 46.dp,
                        iconSize = 22.dp,
                        shape = RoundedCornerShape(14.dp),
                    )
                    Spacer(Modifier.size(12.dp))
                    Column {
                        Text(
                            text = "Annette",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = "Your personal finance companion",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            SectionHeader(title = "Customize")
            Spacer(Modifier.height(12.dp))

            GroupedSurface {
                settingsItems.forEachIndexed { index, item ->
                    SettingsRow(item = item)
                    if (index != settingsItems.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 14.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            SectionHeader(title = "About")
            Spacer(Modifier.height(12.dp))

            GroupedSurface {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PastelIconTile(
                        icon = Icons.Rounded.Info,
                        tint = BrandTheme.colors.chart.sky,
                        container = BrandTheme.colors.chart.sky.copy(alpha = 0.14f),
                        size = 40.dp,
                        iconSize = 19.dp,
                        shape = RoundedCornerShape(12.dp),
                    )
                    Spacer(Modifier.size(12.dp))
                    Column {
                        Text(
                            text = "Version",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = "Annette 0.02",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PastelIconTile(
                        icon = Icons.Rounded.Settings,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        container = MaterialTheme.colorScheme.surfaceVariant,
                        size = 40.dp,
                        iconSize = 19.dp,
                        shape = RoundedCornerShape(12.dp),
                    )
                    Spacer(Modifier.size(12.dp))
                    Text(
                        text = "Built with Jetpack Compose",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(Modifier.height(30.dp))
            Text(
                text = "Made with care for calmer money decisions.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
            Spacer(Modifier.height(120.dp))
        }
    }
}

@Composable
private fun SettingsRow(item: SettingsItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = item.onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PastelIconTile(
            icon = item.icon,
            tint = BrandTheme.colors.berry,
            container = BrandTheme.colors.berrySoft,
            size = 42.dp,
            iconSize = 20.dp,
            shape = RoundedCornerShape(13.dp),
        )
        Spacer(Modifier.size(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp),
        )
    }
}

@PreviewLightDark()
@Composable
private fun SettingsScreenPreview() {
    AnnetteTheme {
        SettingsScreen(onSettingsItemClick = {})
    }
}
