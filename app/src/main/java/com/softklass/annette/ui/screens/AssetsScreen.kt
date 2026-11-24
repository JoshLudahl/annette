package com.softklass.annette.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.softklass.annette.data.model.BalanceSheetType
import com.softklass.annette.ui.screens.viewmodels.AssetsViewModel

@Composable
fun AssetsScreen(
    viewModel: AssetsViewModel,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onNavigateToDetail: (Long, String, String, String) -> Unit = { _, _, _, _ -> },
) {
    val assets by viewModel.assets.collectAsState()
    val historicalTotals by viewModel.historicalTotals.collectAsState()
    val showChart by viewModel.showChart.collectAsState()

    AccountsScreenContent(
        title = "Assets",
        type = BalanceSheetType.ASSETS,
        items = assets,
        historicalTotals = historicalTotals,
        showChart = showChart,
        onNavigateBack = onNavigateBack,
        onToggleChart = viewModel::toggleChart,
        onAddItem = { name, amount, category, date ->
            viewModel.addAsset(name, amount, category, date)
        },
        onDelete = viewModel::deleteAsset,
        onNavigateToDetail = onNavigateToDetail,
        modifier = modifier,
    )
}
