package com.softklass.annette.feature.budget.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softklass.annette.feature.budget.data.model.BudgetItemType
import com.softklass.annette.feature.budget.ui.components.AddBudgetItemBottomSheet
import com.softklass.annette.feature.budget.ui.components.BudgetFloatingActionButton
import kotlinx.coroutines.launch

enum class BudgetScreenTab {
    DASHBOARD,
    EXPENSES,
    INCOME
}

data class BudgetEntity(
    val name: String,
    val amount: Double,
    val category: String,
    val type: BudgetItemType,
    val dueDateMillis: Long
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    viewModel: BudgetViewModel
) {
    val titles = BudgetScreenTab.entries

    var state by remember { mutableIntStateOf(0) }
    var type by remember { mutableStateOf(BudgetItemType.INCOME) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()

    val incomeItems by viewModel.budgetIncomeItems.collectAsState()
    val expenseItems by viewModel.budgetExpenseItems.collectAsState()

    Scaffold(
        bottomBar = { },
        floatingActionButton = {
            BudgetFloatingActionButton(
                onClick = {
                    scope.launch {
                        bottomSheetState.show()
                    }
                    type = it
                }
            )
        },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.End,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                SecondaryTabRow(selectedTabIndex = state) {
                    titles.forEachIndexed { index, title ->
                        FancyTab(
                            title = title.toString(),
                            onClick = { state = index },
                            selected = (index == state)
                        )

                    }
                }

                BudgetScreenHost(
                    state = state,
                    viewModel = viewModel,
                    lineItems = incomeItems,
                    expenseItems = expenseItems
                )
            }

            AnimatedVisibility(visible = bottomSheetState.isVisible) {
                AddBudgetItemBottomSheet(
                    sheetState = bottomSheetState,
                    onDismissRequest = {
                        scope.launch {
                            bottomSheetState.hide()
                        }
                    },
                    initialType = type,
                    onAddBudgetItem = {
                        viewModel.addBudgetItem(it)
                        scope.launch {
                            bottomSheetState.hide()
                        }
                    }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun BudgetScreenHost(
    state: Int,
    viewModel: BudgetViewModel,
    lineItems: List<BudgetEntity> = emptyList(),
    expenseItems: List<BudgetEntity> = emptyList()
) {
    when (state) {
        0 -> DashboardTabContent(viewModel)
        1 -> ExpensesTabContent(viewModel = viewModel, expenseItems = expenseItems)
        2 -> IncomeTabContent(viewModel = viewModel, lineItems = lineItems)
        else -> throw NoSuchElementException()
    }
}

@Composable
fun FancyTab(title: String, onClick: () -> Unit, selected: Boolean) {
    Tab(selected, onClick) {
        Column(
            Modifier
                .padding(10.dp)
                .height(50.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                Modifier
                    .size(10.dp)
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
                color =
                    if (selected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary
            )
        }
    }
}
