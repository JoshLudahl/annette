package com.softklass.annette.feature.budget.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class BudgetScreenTab {
    DASHBOARD,
    EXPENSES,
    INCOME
}

@Composable
fun BudgetScreen(
) {
    val titles = BudgetScreenTab.entries

    var state by remember { mutableIntStateOf(0) }
    Column {
        SecondaryTabRow(selectedTabIndex = state) {
            titles.forEachIndexed { index, title ->
                FancyTab(title = title.toString(), onClick = { state = index }, selected = (index == state))

            }
        }
        BudgetScreenHost(state = state)
    }
}

@Composable
fun BudgetScreenHost(
    state: Int
) {

    when (state) {
        0 -> DashboardTabContent()
        1 -> ExpensesTabContent()
        2 -> IncomeTabContent()
        else -> throw NoSuchElementException()
    }
}

@Composable
fun DashboardTabContent() {
    // todo: implement dashboard content
    Text("Dashboard content")
}

@Composable
fun ExpensesTabContent() {
    // todo: implement expenses content
    Text("Expenses content")
}

@Composable
fun IncomeTabContent() {
    // todo: implement income content
    Text("Income content")
}

@Composable
fun FancyTab(title: String, onClick: () -> Unit, selected: Boolean) {
    Tab(selected, onClick) {
        Column(
            Modifier.padding(10.dp).height(50.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                Modifier.size(10.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(
                        color =
                            if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.background
                    )
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }
    }
}
