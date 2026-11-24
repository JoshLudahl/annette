package com.softklass.annette.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ShowChart
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.softklass.annette.core.ui.currency.currencyFormatter
import com.softklass.annette.core.ui.design.AnnettePageHeader
import com.softklass.annette.core.ui.design.CircleIconButton
import com.softklass.annette.core.ui.design.EmptyState
import com.softklass.annette.core.ui.design.GroupedSurface
import com.softklass.annette.core.ui.design.PastelIconTile
import com.softklass.annette.core.ui.design.SectionHeader
import com.softklass.annette.core.ui.design.categoryIcon
import com.softklass.annette.data.database.dao.BalanceSheetItemWithValue
import com.softklass.annette.data.database.dao.HistoricalTotal
import com.softklass.annette.data.model.BalanceSheetType
import com.softklass.annette.ui.components.AddBalanceSheetItemDialog
import com.softklass.annette.ui.components.ConfirmDeleteItemDialog
import com.softklass.annette.ui.components.HistoricalChart
import com.softklass.theme.ui.theme.BrandTheme
import com.softklass.theme.ui.theme.ExtendedTheme

/**
 * Shared layout for the Assets and Liabilities screens. Both screens surface the same structure:
 * a tinted summary hero, an optional history chart, and a grouped account list with delete support.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AccountsScreenContent(
    title: String,
    type: BalanceSheetType,
    items: List<BalanceSheetItemWithValue>,
    historicalTotals: List<HistoricalTotal>,
    showChart: Boolean,
    onNavigateBack: () -> Unit,
    onToggleChart: () -> Unit,
    onAddItem: (String, Double, String, Long) -> Unit,
    onDelete: (BalanceSheetItemWithValue) -> Unit,
    onNavigateToDetail: (Long, String, String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var itemToDelete by remember { mutableStateOf<BalanceSheetItemWithValue?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    val isAsset = type == BalanceSheetType.ASSETS
    val accent = if (isAsset) ExtendedTheme.colors.asset.color else ExtendedTheme.colors.liability.color
    val accentContainer =
        if (isAsset) ExtendedTheme.colors.asset.colorContainer else ExtendedTheme.colors.liability.colorContainer
    val onAccent = if (isAsset) ExtendedTheme.colors.asset.onColor else ExtendedTheme.colors.liability.onColor
    val heroContent =
        if (isAsset) ExtendedTheme.colors.asset.onColorContainer else ExtendedTheme.colors.liability.onColorContainer
    val total = items.sumOf { it.value ?: 0.0 }
    val grouped = items.groupBy { it.category.ifBlank { "Uncategorized" } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        AnnettePageHeader(
            title = title,
            subtitle = "${items.size} ${if (items.size == 1) "account" else "accounts"}",
            onBack = onNavigateBack,
            actions = {
                CircleIconButton(
                    icon = if (showChart) Icons.Rounded.Timeline else Icons.Rounded.ShowChart,
                    onClick = onToggleChart,
                    contentDescription = if (showChart) "Hide history" else "Show history",
                )
                Spacer(Modifier.size(8.dp))
                CircleIconButton(
                    icon = Icons.Rounded.Add,
                    onClick = { showAddDialog = true },
                    containerColor = accent,
                    contentColor = onAccent,
                    contentDescription = "Add entry",
                )
            },
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    color = accentContainer,
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PastelIconTile(
                                icon = if (isAsset) {
                                    Icons.Rounded.AccountBalance
                                } else {
                                    Icons.Rounded.AccountBalanceWallet
                                },
                                tint = accent,
                                container = accent.copy(alpha = 0.18f),
                                size = 42.dp,
                                iconSize = 21.dp,
                            )
                            Spacer(Modifier.size(12.dp))
                            Column {
                                Text(
                                    text = "Total $title",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = heroContent.copy(alpha = 0.7f),
                                )
                                Text(
                                    text = "${items.size} ${if (items.size == 1) "entry" else "entries"} tracked",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = heroContent.copy(alpha = 0.55f),
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = currencyFormatter.format(total),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = heroContent,
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

            if (showChart) {
                item {
                    GroupedSurface {
                        Column(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)) {
                            Text(
                                text = "$title history",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 16.dp),
                            )
                            HistoricalChart(historicalTotals = historicalTotals)
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                }
            }

            item {
                SectionHeader(title = "Your $title")
                Spacer(Modifier.height(12.dp))
            }

            if (items.isEmpty()) {
                item {
                    GroupedSurface {
                        EmptyState(
                            icon = if (isAsset) Icons.Rounded.AccountBalance else Icons.Rounded.AccountBalanceWallet,
                            title = "No $title yet",
                            message = "Tap the + button above to add your first entry.",
                        )
                    }
                }
            } else {
                grouped.forEach { (category, categoryItems) ->
                    stickyHeader(key = "$title-$category") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(top = 4.dp, bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = currencyFormatter.format(categoryItems.sumOf { it.value ?: 0.0 }),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }

                    item(key = "$title-$category-card") {
                        GroupedSurface {
                            categoryItems.forEachIndexed { index, item ->
                                AccountItemRow(
                                    item = item,
                                    accent = accent,
                                    accentContainer = accentContainer,
                                    onClick = {
                                        onNavigateToDetail(item.id, item.name, item.category, item.type)
                                    },
                                    onLongPress = { itemToDelete = item },
                                )
                                if (index != categoryItems.lastIndex) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 14.dp),
                                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddBalanceSheetItemDialog(
            onDismiss = { showAddDialog = false },
            onAddItem = { name, amount, category, date ->
                onAddItem(name, amount, category, date)
                showAddDialog = false
            },
            type = type,
        )
    }

    val pendingDelete = itemToDelete
    if (pendingDelete != null) {
        ConfirmDeleteItemDialog(
            itemName = pendingDelete.name,
            onDismiss = { itemToDelete = null },
            onConfirmDelete = {
                onDelete(pendingDelete)
                itemToDelete = null
            },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AccountItemRow(
    item: BalanceSheetItemWithValue,
    accent: androidx.compose.ui.graphics.Color,
    accentContainer: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongPress)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PastelIconTile(
            icon = categoryIcon(item.category),
            tint = accent,
            container = accentContainer.copy(alpha = 0.9f),
            size = 42.dp,
            iconSize = 20.dp,
            shape = RoundedCornerShape(13.dp),
        )
        Spacer(Modifier.size(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item.category.ifBlank { "Uncategorized" },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Spacer(Modifier.size(8.dp))
        Text(
            text = currencyFormatter.format(item.value ?: 0.0),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = accent,
        )
    }
}
