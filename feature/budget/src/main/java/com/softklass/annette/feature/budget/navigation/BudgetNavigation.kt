package com.softklass.annette.feature.budget.navigation


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.softklass.annette.feature.budget.ui.BudgetScreen

fun NavGraphBuilder.budgetScreen(navController: NavController) {
    composable(route = "budget") {
        BudgetScreen()
    }
}

fun NavController.navigateToBudget() {
    this.navigate("budget")
}