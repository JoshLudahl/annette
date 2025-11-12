package com.softklass.annette.feature.budget.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softklass.annette.core.model.Budget
import com.softklass.annette.core.ui.composables.BudgetInfoHeader
import com.softklass.annette.feature.budget.ui.components.BudgetLineItemList

@Composable
fun ExpensesTabContent(
    viewModel: BudgetViewModel,
    expenseItems: List<BudgetEntity>
) {
    val totalExpenses by viewModel.totalExpenses.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BudgetInfoHeader(
            total = totalExpenses,
            type = Budget.EXPENSE
        )

        BudgetLineItemList(
            items = expenseItems,
            onItemDelete = { viewModel.deleteBudgetItem(it) }
        )
    }
}
