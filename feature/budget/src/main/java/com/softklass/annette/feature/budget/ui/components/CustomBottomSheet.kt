package com.softklass.annette.feature.budget.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.softklass.annette.feature.budget.data.model.BudgetItemType
import com.softklass.annette.feature.budget.ui.screens.BudgetEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBudgetItemBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    initialType: BudgetItemType = BudgetItemType.INCOME,
    onAddBudgetItem: (BudgetEntity) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(initialType) }

    var showDatePicker by remember { mutableStateOf(false) }
    var dueDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }

    val dateFormatter = remember {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(),
            sheetState = sheetState,
            onDismissRequest = onDismissRequest
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Add Budget Item",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it.trimStart().trimEnd() },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences,
                    ),
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it.replace(",", "").filter { ch -> ch.isDigit() || ch == '.' }
                    },
                    label = { Text("Amount") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next,
                    ),
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it.trimStart() },
                    label = { Text("Category") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next,
                    ),
                )

                Text(
                    text = "Type",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = type == BudgetItemType.INCOME,
                        onClick = { type = BudgetItemType.INCOME }
                    )
                    Text(
                        text = "Income",
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    RadioButton(
                        selected = type == BudgetItemType.EXPENSE,
                        onClick = { type = BudgetItemType.EXPENSE }
                    )
                    Text(text = "Expense")
                }

                Text(
                    text = "Due",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = dateFormatter.format(Date(dueDateMillis)),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    TextButton(onClick = { showDatePicker = true }) {
                        Text("Select date")
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = {
                            val cleanedAmount = amount.replace(",", "")
                            val parsed = cleanedAmount.toDoubleOrNull()
                            if (
                                name.isNotBlank() &&
                                category.isNotBlank() &&
                                parsed != null && parsed > 0.0
                            ) {
                                onAddBudgetItem(
                                    BudgetEntity(
                                        name = name.trim(),
                                        amount = parsed,
                                        category = category.trim(),
                                        type = type,
                                        dueDateMillis = dueDateMillis
                                    )
                                )
                            }
                        },
                        enabled = name.isNotBlank() && amount.isNotBlank() && category.isNotBlank()
                    ) {
                        Text("Add")
                    }
                }
            }

            if (showDatePicker) {
                val state = rememberDatePickerState(
                    initialSelectedDateMillis = dueDateMillis
                )

                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val selected = state.selectedDateMillis
                                if (selected != null) {
                                    dueDateMillis = selected
                                }
                                showDatePicker = false
                            }
                        ) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                    }
                ) {
                    DatePicker(state = state)
                }
            }
        }
    }
}
