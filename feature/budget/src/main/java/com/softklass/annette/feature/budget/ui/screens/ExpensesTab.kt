package com.softklass.annette.feature.budget.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softklass.annette.feature.budget.data.model.BudgetItemType
import com.softklass.annette.feature.budget.ui.components.BudgetLineItemList

@Composable
fun ExpensesTabContent(
    viewModel: BudgetViewModel,
    expenseItems: List<BudgetEntity>
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BudgetLineItemList(items = expenseItems)
    }
}
