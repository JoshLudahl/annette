package com.softklass.annette.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Title
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.softklass.annette.core.ui.currency.amountFormatted
import com.softklass.annette.data.model.BalanceSheetType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBalanceSheetItemDialog(
    onDismiss: () -> Unit,
    onAddItem: (String, Double, String, Long) -> Unit, // Added Long for date
    type: BalanceSheetType
) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var selectedDate by remember { mutableLongStateOf(Calendar.getInstance().timeInMillis) }
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)
    val dateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

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
                val label = when (type) {
                    BalanceSheetType.ASSETS -> Pair("Add Asset", "Asset Name")
                    BalanceSheetType.LIABILITIES -> Pair("Add Liability", "Liability Name")
                }

                Text(
                    text = label.first,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it.trimStart() },
                    label = { Text(label.second) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions =
                        KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences,
                        ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Title,
                            contentDescription = "Title",

                            )
                    },
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it.amountFormatted
                    },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next,
                    ),
                    maxLines = 1,
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            Icons.Rounded.AttachMoney,
                            contentDescription = "Amount"
                        )
                    },
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it.trimStart() },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("e.g., Real Estate, Cash, Investments") },
                    keyboardOptions =
                        KeyboardOptions(
                            imeAction = ImeAction.Next, // Changed from Done to Next
                            capitalization = KeyboardCapitalization.Sentences,
                        ),
                    leadingIcon = { Icon(Icons.Rounded.Category, contentDescription = "Category") },
                )

                OutlinedTextField(
                    value = dateFormatter.format(Date(selectedDate)),
                    onValueChange = { /* Read-only */ },
                    label = { Text("Date") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true },
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

                if (showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                datePickerState.selectedDateMillis?.let {
                                    selectedDate = it
                                }
                                showDatePicker = false
                            }) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePicker = false }) {
                                Text("Cancel")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                val errorMessage = remember { mutableStateOf(false) }

                if (errorMessage.value) {
                    Text(
                        text = "Please fill all fields correctly",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (name.isNotBlank() && amount.isNotBlank() && category.isNotBlank()) {
                                val amountDouble = amount.toDoubleOrNull()
                                if (amountDouble != null && amountDouble > 0) {
                                    // Pass selectedDate to onAddItem
                                    onAddItem(
                                        name.trim(),
                                        amountDouble,
                                        category.trim(),
                                        selectedDate
                                    )
                                } else {
                                    Log.e("AddItemDialog", "Invalid amount: $amount")
                                    errorMessage.value = true
                                }
                            } else {
                                Log.e(
                                    "AddItemDialog",
                                    "Missing required fields; name: $name, amount: $amount, category: $category"
                                )
                                errorMessage.value = true
                            }
                        },
                        // The button is enabled if all fields are filled, date is always selected by default
                        enabled = name.isNotBlank() && amount.isNotBlank() && category.isNotBlank()
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}
