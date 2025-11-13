package com.softklass.annette.feature.budget.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softklass.annette.core.ui.composables.BudgetInfoHeader
import com.softklass.annette.core.ui.composables.DisplayIncomeExpenseCards
import com.softklass.annette.core.ui.composables.Donut
import com.softklass.annette.core.ui.extractProportions
import com.softklass.theme.ui.theme.ExtendedTheme

@Composable
fun DashboardTabContent(viewModel: BudgetViewModel) {
    val income by viewModel.totalIncome.collectAsState()
    val expenses by viewModel.totalExpenses.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val net = income - expenses
        val dti = ((expenses / income) * 100).toInt()
        Box(
            modifier = Modifier.padding(
                bottom = 20.dp,
            )
        ) {
            BudgetInfoHeader(
                total = net
            )
        }

        Column {
            Box(Modifier.padding(16.dp)) {
                val list = listOf(income, expenses)
                val accountsProportion = list.extractProportions { it.toFloat() }

                Donut(
                    proportions = accountsProportion,
                    colors = listOf(ExtendedTheme.colors.asset.color, ExtendedTheme.colors.liability.color),
                    Modifier
                        .height(300.dp)
                        .align(Alignment.Center)
                        .fillMaxWidth()
                )

                Column(modifier = Modifier.align(Alignment.Center)) {
                    Text(
                        text = "DTI",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "$dti%",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
            Spacer(Modifier.height(10.dp))

        }

        Spacer(modifier = Modifier.height(16.dp))

        DisplayIncomeExpenseCards(
            income = income,
            expense = expenses,
        )
    }
}
