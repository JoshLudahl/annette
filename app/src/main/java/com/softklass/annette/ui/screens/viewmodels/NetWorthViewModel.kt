package com.softklass.annette.ui.screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softklass.annette.data.database.dao.BalanceSheetDao
import com.softklass.annette.feature.budget.data.database.dao.BudgetDao
import com.softklass.annette.feature.budget.data.model.BudgetItemType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetWorthViewModel @Inject constructor(
    private val balanceSheetDao: BalanceSheetDao,
    private val budgetDao: BudgetDao,
) : ViewModel() {

    private val _totalAssets = MutableStateFlow(0.0)
    val totalAssets: StateFlow<Double> = _totalAssets.asStateFlow()

    private val _totalLiabilities = MutableStateFlow(0.0)
    val totalLiabilities: StateFlow<Double> = _totalLiabilities.asStateFlow()

    private val _netWorth = MutableStateFlow(0.0)
    val netWorth: StateFlow<Double> = _netWorth.asStateFlow()

    private val _incomeTotal = MutableStateFlow(0.0)
    val incomeTotal: StateFlow<Double> = _incomeTotal.asStateFlow()

    private val _expenseTotal = MutableStateFlow(0.0)
    val expenseTotal: StateFlow<Double> = _expenseTotal.asStateFlow()

    init {
        loadTotalAssets()
        loadTotalLiabilities()
        loadIncomeExpenseTotals()
        calculateNetWorth()
    }

    private fun loadTotalAssets() {
        viewModelScope.launch {
            balanceSheetDao.getAssetsWithLatestValues().collect { assets ->
                _totalAssets.value = assets.sumOf { it.value ?: 0.0 }
            }
        }
    }

    private fun loadTotalLiabilities() {
        viewModelScope.launch {
            balanceSheetDao.getLiabilitiesWithLatestValues().collect { liabilities ->
                _totalLiabilities.value = liabilities.sumOf { it.value ?: 0.0 }
            }
        }
    }

    private fun loadIncomeExpenseTotals() {
        viewModelScope.launch {
            budgetDao.getTotalValueByType(BudgetItemType.INCOME.name).collect { total ->
                _incomeTotal.value = total ?: 0.0
            }
        }
        viewModelScope.launch {
            budgetDao.getTotalValueByType(BudgetItemType.EXPENSE.name).collect { total ->
                _expenseTotal.value = total ?: 0.0
            }
        }
    }

    private fun calculateNetWorth() {
        viewModelScope.launch {
            combine(_totalAssets, _totalLiabilities) { assets, liabilities ->
                assets - liabilities
            }.collect { netWorth ->
                _netWorth.value = netWorth
            }
        }
    }
}