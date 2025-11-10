package com.softklass.annette.feature.budget.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.softklass.annette.feature.budget.ui.screens.BudgetEntity
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BudgetLineItemList(
    items: List<BudgetEntity>,
    modifier: Modifier = Modifier,
    onItemClick: ((BudgetEntity) -> Unit)? = null,
    onItemDelete: ((BudgetEntity) -> Unit)? = null,
) {
    val groupedItems = items.groupBy { it.category }

    // Dialog state for delete confirmation
    val selectedItem = remember { mutableStateOf<BudgetEntity?>(null) }
    val showDialog = remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = groupedItems.values.flatten(),
            key = { it.name + "|" + it.category + "|" + it.dueDateMillis },
            contentType = { _ -> "budgetItem" }
        ) { entity ->
            BudgetLineItemRow(
                name = entity.name,
                category = entity.category,
                amount = entity.amount,
                dateMillis = entity.dueDateMillis,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surface)
                    .combinedClickable(
                        onClick = { onItemClick?.invoke(entity) },
                        onLongClick = {
                            selectedItem.value = entity
                            showDialog.value = true
                        }
                    ),
                onClick = { onItemClick?.invoke(entity) }
            )
        }
        item { Spacer(modifier = Modifier.height(4.dp)) }
    }

    if (showDialog.value && selectedItem.value != null) {
        ConfirmDeleteBudgetItemDialog(
            itemName = selectedItem.value!!.name,
            onDismiss = {
                showDialog.value = false
                selectedItem.value = null
            },
            onConfirmDelete = {
                onItemDelete?.invoke(selectedItem.value!!)
                showDialog.value = false
                selectedItem.value = null
            }
        )
    }
}

@Composable
fun BudgetLineItemRow(
    name: String,
    category: String,
    amount: Double,
    dateMillis: Long,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    // Formatters local to feature module (avoid cross-module dependency)
    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale.US)
    }
    val dateFormatter = remember {
        DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.getDefault())
    }

    val dateText = remember(dateMillis) {
        Instant.ofEpochMilli(dateMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(dateFormatter)
    }

    // Rounded icon letter from category (fallback to name first letter if needed)
    val letter = remember(name, category) {
        (category.firstOrNull() ?: name.firstOrNull() ?: '•')
            .uppercaseChar()
    }

    Surface(
        modifier = modifier,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Leading rounded icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .alpha(0.6f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            // Middle column: name and date
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = dateText,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            // Right column: amount and category, aligned to end
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = currencyFormatter.format(amount),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = category,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
