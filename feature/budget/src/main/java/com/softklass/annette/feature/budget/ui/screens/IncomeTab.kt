package com.softklass.annette.feature.budget.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softklass.annette.feature.budget.ui.components.BudgetLineItemList

@Composable
fun IncomeTabContent(
    viewModel: BudgetViewModel,
    lineItems: List<BudgetEntity>
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IncomeTabDetails(
            lineItems = lineItems,
            onDelete = { viewModel.deleteBudgetItem(it) }
        )
    }
}

@Composable
fun IncomeTabDetails(
    lineItems: List<BudgetEntity>,
    onDelete: ((BudgetEntity) -> Unit)? = null,
) {
    BudgetLineItemList(
        items = lineItems,
        onItemDelete = onDelete
    )
}
