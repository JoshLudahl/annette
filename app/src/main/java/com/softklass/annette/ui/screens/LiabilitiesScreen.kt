package com.softklass.annette.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softklass.annette.data.model.BalanceSheetType
import com.softklass.annette.ui.components.AddBalanceSheetItemDialog
import com.softklass.annette.ui.components.BalanceSheetHeaderCard
import com.softklass.annette.ui.components.BalanceSheetItemList
import com.softklass.annette.ui.components.BalanceSheetRoundedCardContainer
import com.softklass.annette.ui.components.EmptyBalanceSheetListCard
import com.softklass.annette.ui.components.HistoricalChart
import com.softklass.annette.ui.screens.viewmodels.LiabilitiesViewModel
import com.softklass.theme.ui.theme.AnnetteTheme
import com.softklass.theme.ui.theme.ExtendedTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiabilitiesScreen(
    modifier: Modifier = Modifier,
    viewModel: LiabilitiesViewModel,
    onNavigateToDetail: (Long, String, String, String) -> Unit = { _, _, _, _ -> }
) {
    val liabilities by viewModel.liabilities.collectAsState()
    val historicalTotals by viewModel.historicalTotals.collectAsState()
    val showChart by viewModel.showChart.collectAsState()
    val showAddDialog by viewModel.showAddDialog.collectAsState()

    var itemToDelete by remember {
        mutableStateOf<com.softklass.annette.data.database.dao.BalanceSheetItemWithValue?>(
            null
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            ) {
                BalanceSheetHeaderCard(
                    items = liabilities,
                    type = BalanceSheetType.LIABILITIES,
                    icon = Icons.Rounded.AccountBalanceWallet,
                    onClickIcon = { }
                )
            }

            // Chart Section
            if (showChart) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Liabilities History",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        HistoricalChart(historicalTotals = historicalTotals)
                    }
                }
            }

            // Add Button and Chart Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Liabilities",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Chart Toggle Button
                    FloatingActionButton(
                        onClick = { viewModel.toggleChart() },
                        modifier = Modifier.size(48.dp),
                        containerColor = if (showChart) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.TrendingUp,
                            contentDescription = if (showChart) "Hide Chart" else "Show Chart",
                            tint = if (showChart) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Add Liability Button
                    FloatingActionButton(
                        onClick = { viewModel.showAddDialog() },
                        modifier = Modifier.size(48.dp),
                        containerColor = ExtendedTheme.colors.liability.colorContainer
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Liability",
                            tint = ExtendedTheme.colors.liability.onColorContainer
                        )
                    }
                }
            }

            // Liabilities List
            if (liabilities.isEmpty()) {
                EmptyBalanceSheetListCard()
            } else {

                BalanceSheetRoundedCardContainer {
                    BalanceSheetItemList(
                        liabilities,
                        onNavigateToDetail = { id, name, amount, category ->
                            onNavigateToDetail(id.toLong(), name, amount, category)
                        },
                        onLonPress = { item ->
                            itemToDelete = item
                        }
                    )
                }
            }
        }

        // Add Liability Dialog
        if (showAddDialog) {
            AddBalanceSheetItemDialog(
                onDismiss = { viewModel.hideAddDialog() },
                onAddItem = { name: String, amount: Double, category: String, date: Long ->
                    viewModel.addLiability(name, amount, category, date)
                },
                type = BalanceSheetType.LIABILITIES
            )
        }

        // Delete confirm dialog
        val pendingDelete = itemToDelete
        if (pendingDelete != null) {
            com.softklass.annette.ui.components.ConfirmDeleteItemDialog(
                itemName = pendingDelete.name,
                onDismiss = { itemToDelete = null },
                onConfirmDelete = {
                    viewModel.deleteLiability(pendingDelete)
                    itemToDelete = null
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LiabilitiesScreenPreview() {
    AnnetteTheme {
    }
}
