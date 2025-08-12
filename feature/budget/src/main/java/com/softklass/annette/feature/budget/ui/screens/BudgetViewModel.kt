package com.softklass.annette.feature.budget.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softklass.annette.feature.budget.data.database.dao.BudgetDao
import com.softklass.annette.feature.budget.data.database.entities.BudgetValue
import com.softklass.annette.feature.budget.data.database.entities.budgetEntityToBudgetItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetDao: BudgetDao
) : ViewModel() {

    fun addBudgetItem(entity: BudgetEntity) {
        val budgetItem = budgetEntityToBudgetItem(entity)
        viewModelScope.launch {
            budgetDao.insertBudgetItem(budgetItem)
            budgetDao.insertBudgetValue(
                BudgetValue(
                    parentId = budgetItem.id,
                    value = entity.amount,
                )
            )

            budgetDao.getAllBudgetItems().collect {
                println(it)
            }
        }
    }
}
