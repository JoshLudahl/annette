package com.softklass.annette.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.softklass.annette.currencyFormatter
import com.softklass.annette.data.database.entities.BalanceSheetValues
import com.softklass.annette.ui.screens.viewmodels.ItemDetailViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    itemId: Long,
    itemName: String,
    itemCategory: String,
    itemType: String,
    onNavigateBack: () -> Unit,
    viewModel: ItemDetailViewModel = hiltViewModel()
) {
    val item by viewModel.item.collectAsState()
    val historicalValues by viewModel.historicalValues.collectAsState()
    val showAddValueDialog by viewModel.showAddValueDialog.collectAsState()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()

    LaunchedEffect(itemId) {
        viewModel.loadItemDetails(itemId, itemName, itemCategory, itemType)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(itemName) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddValueDialog() }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Value")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Historical Chart Placeholder
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Historical Chart",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Chart implementation coming soon",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Historical Values List
            Text(
                text = "Historical Values",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(historicalValues) { value ->
                    HistoricalValueItem(
                        value = value,
                        onLongClick = { viewModel.showDeleteDialog(value) }
                    )
                }
            }
        }
    }

    // Add Value Dialog
    if (showAddValueDialog) {
        AddValueDialog(
            onDismiss = { viewModel.hideAddValueDialog() },
            onAddValue = { amount -> viewModel.addValue(amount) }
        )
    }

    // Delete Confirmation Dialog
    showDeleteDialog?.let { valueToDelete ->
        DeleteValueDialog(
            value = valueToDelete,
            onDismiss = { viewModel.hideDeleteDialog() },
            onConfirm = { viewModel.deleteValue(valueToDelete) }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoricalValueItem(
    value: BalanceSheetValues,
    onLongClick: () -> Unit
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { /* Regular click - no action */ },
                onLongClick = onLongClick
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = currencyFormat.format(value.value),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = dateFormat.format(Date(value.date)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AddValueDialog(
    onDismiss: () -> Unit,
    onAddValue: (Double) -> Unit
) {
    var valueText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Add New Value",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = valueText,
                    onValueChange = { valueText = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            valueText.toDoubleOrNull()?.let { amount ->
                                onAddValue(amount)
                            }
                        },
                        enabled = valueText.toDoubleOrNull() != null
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteValueDialog(
    value: BalanceSheetValues,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Value") },
        text = {
            Text("Are you sure you want to delete the value ${currencyFormat.format(value.value)} from ${dateFormat.format(Date(value.date))}?")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}