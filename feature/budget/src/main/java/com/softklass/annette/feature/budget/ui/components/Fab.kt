package com.softklass.annette.feature.budget.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.softklass.annette.feature.budget.data.model.BudgetItemType

data class FloatingActionButtonMenuItem(
    val icon: ImageVector,
    val text: String,
    val type: BudgetItemType
)

@Composable
fun BudgetFloatingActionButton(
    onClick: (BudgetItemType) -> Unit,
) {
    val items = listOf(
        FloatingActionButtonMenuItem(
            icon = Icons.Rounded.MonetizationOn,
            text = "Add Income",
            type = BudgetItemType.INCOME,
        ),
        FloatingActionButtonMenuItem(
            icon = Icons.Filled.PriceChange,
            text = "Add Expense",
            type = BudgetItemType.EXPENSE,
        ),
    )

    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(contentAlignment = Alignment.BottomEnd) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(end = 16.dp, bottom = 16.dp),
        ) {
            if (expanded) {
                items.forEach { item ->
                    ExtendedFloatingActionButton(
                        text = { Text(item.text) },
                        icon = { Icon(item.icon, contentDescription = null) },
                        onClick = {
                            expanded = false
                            onClick(item.type)
                        },
                    )
                }
            }

            FloatingActionButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.Close else Icons.Filled.Add,
                    contentDescription = if (expanded) "Close" else "Add",
                )
            }
        }
    }
}
