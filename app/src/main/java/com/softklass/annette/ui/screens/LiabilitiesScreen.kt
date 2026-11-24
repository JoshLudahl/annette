package com.softklass.annette.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.softklass.annette.data.model.BalanceSheetType
import com.softklass.annette.ui.screens.viewmodels.LiabilitiesViewModel

@Composable
fun LiabilitiesScreen(
    viewModel: LiabilitiesViewModel,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onNavigateToDetail: (Long, String, String, String) -> Unit = { _, _, _, _ -> },
) {
    val liabilities by viewModel.liabilities.collectAsState()
    val historicalTotals by viewModel.historicalTotals.collectAsState()
    val showChart by viewModel.showChart.collectAsState()

    AccountsScreenContent(
        title = "Liabilities",
        type = BalanceSheetType.LIABILITIES,
        items = liabilities,
        historicalTotals = historicalTotals,
        showChart = showChart,
        onNavigateBack = onNavigateBack,
        onToggleChart = viewModel::toggleChart,
        onAddItem = { name, amount, category, date ->
            viewModel.addLiability(name, amount, category, date)
        },
        onDelete = viewModel::deleteLiability,
        onNavigateToDetail = onNavigateToDetail,
        modifier = modifier,
    )
}
