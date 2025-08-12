package com.softklass.annette.feature.budget.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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

    Column {
        SecondaryTabRow(selectedTabIndex = state) {
            titles.forEachIndexed { index, title ->
                FancyTab(
                    title = title.toString(),
                    onClick = { state = index },
                    selected = (index == state)
                )

            }
        }

        BudgetScreenHost(state = state)

        Spacer(modifier = Modifier.weight(1f))

        BudgetFloatingActionButton(
            onClick = {
                scope.launch {
                    bottomSheetState.show()
                }
                type = it
            }
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
            )
        }
    }
}
