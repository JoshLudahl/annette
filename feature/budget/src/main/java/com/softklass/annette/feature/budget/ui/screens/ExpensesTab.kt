package com.softklass.annette.feature.budget.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.softklass.annette.core.ui.composables.RoundedIconDisplay
import com.softklass.annette.core.ui.currency.currencyFormatter
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
        // Info section similar to Dashboard: shows Total Expenses
        Box(modifier = Modifier.padding(top = 4.dp)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RoundedIconDisplay(
                        icon = Icons.Rounded.Info,
                        iconContainerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f),
                        iconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        onClickIcon = { }
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currencyFormatter.format(totalExpenses),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = "Total expenses",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }

        BudgetLineItemList(
            items = expenseItems,
            onItemDelete = { viewModel.deleteBudgetItem(it) }
        )
    }
}
