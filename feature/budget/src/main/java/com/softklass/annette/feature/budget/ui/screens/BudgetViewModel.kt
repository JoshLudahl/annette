package com.softklass.annette.feature.budget.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softklass.annette.feature.budget.data.database.dao.BudgetDao
import com.softklass.annette.feature.budget.data.database.entities.BudgetItem
import com.softklass.annette.feature.budget.data.database.entities.BudgetValue
import com.softklass.annette.feature.budget.data.model.BudgetItemType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetDao: BudgetDao
) : ViewModel() {

    private val _budgetIncomeItems = getBudgetEntitiesByType(BudgetItemType.INCOME)
    val budgetIncomeItems: StateFlow<List<BudgetEntity>>
        get() = _budgetIncomeItems.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    private val _budgetExpenseItems = getBudgetEntitiesByType(BudgetItemType.EXPENSE)
    val budgetExpenseItems: StateFlow<List<BudgetEntity>>
        get() = _budgetExpenseItems.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    fun addBudgetItem(entity: BudgetEntity) {
        val budgetItem = BudgetItem(
            name = entity.name,
            category = entity.category,
            type = entity.type,
            dueDate = entity.dueDateMillis
        )

        viewModelScope.launch {
            val id = budgetDao.insertBudgetItem(budgetItem)
            budgetDao.insertBudgetValue(
                BudgetValue(
                    parentId = id,
                    value = entity.amount,
                )
            )
        }
    }

    fun deleteBudgetItem(entity: BudgetEntity) {
        // Resolve the DB row for this entity and delete it along with its values
        viewModelScope.launch {
            val item = budgetDao.getBudgetItem(
                name = entity.name,
                category = entity.category,
                type = entity.type.name,
                dueDateMillis = entity.dueDateMillis
            )
            if (item != null) {
                // Delete child values then parent item
                budgetDao.deleteAllBudgetValuesByParentId(item.id)
                budgetDao.deleteBudgetItem(item)
            }
        }
    }

    fun getBudgetEntitiesByType(type: BudgetItemType): Flow<List<BudgetEntity>> {
        val itemsFlow = budgetDao.getBudgetItemsByType(type.name)
        val valuesFlow = budgetDao.getAllBudgetValues()
        return combine(itemsFlow, valuesFlow) { items, values ->
            items.map { item ->
                val amount = values.asSequence()
                    .filter { it.parentId == item.id }
                    .maxByOrNull { it.date }
                    ?.value ?: 0.0
                BudgetEntity(
                    name = item.name,
                    amount = amount,
                    category = item.category,
                    type = item.type,
                    dueDateMillis = item.dueDate
                )
            }
        }
    }
}
