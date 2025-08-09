package com.softklass.annette.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmDeleteItemDialog(
    itemName: String,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Delete item?", color = MaterialTheme.colorScheme.onSurface) },
        text = { Text(text = "Delete '$itemName' and all its values? This action cannot be undone.") },
        confirmButton = {
            TextButton(onClick = onConfirmDelete) {
                Text(text = "Delete", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}
