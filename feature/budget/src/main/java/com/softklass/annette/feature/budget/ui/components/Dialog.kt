package com.softklass.annette.feature.budget.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetDialog(

) {
    AlertDialog(
        onDismissRequest = {  },
        content = {
            Text("Add Income")
        }
    )
}