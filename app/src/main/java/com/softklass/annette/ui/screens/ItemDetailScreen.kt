package com.softklass.annette.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.ShowChart
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.softklass.annette.core.ui.currency.amountFormatted
import com.softklass.annette.core.ui.currency.currencyFormatter
import com.softklass.annette.core.ui.design.AnnettePageHeader
import com.softklass.annette.core.ui.design.CircleIconButton
import com.softklass.annette.core.ui.design.EmptyState
import com.softklass.annette.core.ui.design.GroupedSurface
import com.softklass.annette.core.ui.design.PastelIconTile
import com.softklass.annette.core.ui.design.SectionHeader
import com.softklass.annette.core.ui.design.categoryColors
import com.softklass.annette.core.ui.design.categoryIcon
import com.softklass.theme.ui.theme.BrandTheme
import com.softklass.annette.data.database.entities.BalanceSheetValues
import com.softklass.annette.ui.screens.viewmodels.ItemDetailViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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

    var showEditDialog by remember { mutableStateOf(false) }
    val itemForEdit = item
    var editedName by remember { mutableStateOf(itemForEdit?.name ?: itemName) }
    var editedCategory by remember { mutableStateOf(itemForEdit?.category ?: itemCategory) }

    fun isValidInput(input: String): Boolean {
        return input.isNotBlank() && input.all { it.isLetter() || it.isWhitespace() }
    }

    val currentValue = historicalValues.maxByOrNull { it.date }?.value
    val resolvedType = item?.type ?: itemType
    val isLiability = resolvedType.equals("liability", ignoreCase = true)
    val accent = if (isLiability) BrandTheme.colors.negative else BrandTheme.colors.positive
    val accentContainer = if (isLiability) BrandTheme.colors.negativeSoft else BrandTheme.colors.positiveSoft

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AnnettePageHeader(
                title = item?.name ?: itemName.ifBlank { "Item detail" },
                subtitle = item?.category ?: itemCategory.ifBlank { "Uncategorized" },
                onBack = onNavigateBack,
                modifier = Modifier.statusBarsPadding(),
                actions = {
                    CircleIconButton(
                        icon = Icons.Rounded.Edit,
                        onClick = { showEditDialog = true },
                        contentDescription = "Edit item",
                    )
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddValueDialog() },
                containerColor = BrandTheme.colors.berry,
                contentColor = androidx.compose.ui.graphics.Color.White,
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add value")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(8.dp))

            GroupedSurface {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PastelIconTile(
                        icon = categoryIcon(item?.category ?: itemCategory),
                        tint = accent,
                        container = accentContainer,
                        size = 46.dp,
                        iconSize = 22.dp,
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isLiability) "Current liability" else "Current value",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = currencyFormatter.format(currentValue ?: 0.0),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    if (historicalValues.isNotEmpty()) {
                        Text(
                            text = "${historicalValues.size} updates",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            SectionHeader(title = "Value history")
            Spacer(Modifier.height(10.dp))

            GroupedSurface {
                if (historicalValues.isEmpty()) {
                    EmptyState(
                        icon = Icons.Rounded.ShowChart,
                        title = "No value history",
                        message = "Add a value to start tracking how this item changes over time.",
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(8.dp)
                    ) {
                        com.softklass.annette.ui.components.ItemHistoricalChart(historicalValues)
                    }
                }
            }

            Spacer(Modifier.height(22.dp))

            SectionHeader(title = "Historical values")
            Spacer(Modifier.height(10.dp))

            // Sort values by date ascending so we can calculate percent change
            val sortedValues = historicalValues.sortedBy { it.date }.reversed()

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                sortedValues.forEach { valueItem ->
                    HistoricalValueItem(
                        value = valueItem,
                        onLongClick = { viewModel.showDeleteDialog(valueItem) }
                    )
                }
            }
            Spacer(Modifier.height(80.dp))
        }
    }

    // Add Value Dialog
    if (showAddValueDialog) {
        AddValueDialog(
            onDismiss = { viewModel.hideAddValueDialog() },
            onAddValue = { amount, date -> viewModel.addValue(amount, date) } // Updated lambda
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

    // Edit Item Dialog
    if (showEditDialog) {
        Dialog(onDismissRequest = { showEditDialog = false }) {
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
                        text = "Edit Item",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text("Name") },
                        isError = !isValidInput(editedName),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editedCategory,
                        onValueChange = { editedCategory = it },
                        label = { Text("Category") },
                        isError = !isValidInput(editedCategory),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showEditDialog = false }) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                viewModel.updateItemDetails(
                                    name = editedName.trim(),
                                    category = editedCategory.trim()
                                )
                                showEditDialog = false
                            },
                            enabled = isValidInput(editedName) && isValidInput(editedCategory)
                        ) {
                            Text("Update")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoricalValueItem(
    value: BalanceSheetValues,
    onLongClick: () -> Unit
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { /* Regular click - no action */ },
                onLongClick = onLongClick
            ),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f),
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = dateFormat.format(Date(value.date)),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = currencyFormat.format(value.value),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddValueDialog(
    onDismiss: () -> Unit,
    onAddValue: (Double, Long) -> Unit
) {
    var valueText by remember { mutableStateOf("") }
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var selectedDateMillis by remember {
        mutableStateOf(
            datePickerState.selectedDateMillis ?: System.currentTimeMillis()
        )
    }

    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    } }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Update Value",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = valueText,
                    onValueChange = { valueText = it.amountFormatted },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = dateFormat.format(Date(selectedDateMillis)),
                    onValueChange = { /* Read-only */ },
                    label = { Text("Date") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePickerDialog = true },
                    enabled = false, // To make it non-editable but clickable
                    colors = OutlinedTextFieldDefaults.colors(
                        // Ensure it looks enabled
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    leadingIcon = { Icon(Icons.Rounded.DateRange, contentDescription = "Date") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                )

                if (showDatePickerDialog) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePickerDialog = false },
                        confirmButton = {
                            TextButton(onClick = {
                                datePickerState.selectedDateMillis?.let {
                                    selectedDateMillis = it
                                }
                                showDatePickerDialog = false
                            }) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePickerDialog = false }) {
                                Text("Cancel")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

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
                                onAddValue(amount, selectedDateMillis)
                            }
                        },
                        enabled = valueText.toDoubleOrNull() != null && selectedDateMillis > 0
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDateMillis = millis
                        }
                        showDatePickerDialog = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
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
            Text(
                "Are you sure you want to delete the value ${currencyFormat.format(value.value)} from ${
                    dateFormat.format(
                        Date(value.date)
                    )
                }?"
            )
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
