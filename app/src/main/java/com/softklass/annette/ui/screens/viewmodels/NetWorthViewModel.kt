package com.softklass.annette.ui.screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softklass.annette.data.database.dao.BalanceSheetDao
import com.softklass.annette.feature.budget.data.database.dao.BudgetDao
import com.softklass.annette.feature.budget.data.model.BudgetItemType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** A single recent income/expense entry, ready for the Home activity list. */
data class TransactionPreview(
    val name: String,
    val category: String,
    val type: BudgetItemType,
    val amount: Double,
    val date: Long,
)

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

    /** Latest budget entries, newest first, used for the Home "Transactions" section. */
    val recentTransactions: StateFlow<List<TransactionPreview>> =
        combine(
            budgetDao.getAllBudgetItems(),
            budgetDao.getAllBudgetValues(),
        ) { items, values ->
            val latestByParent = values
                .groupBy { it.parentId }
                .mapValues { (_, entries) -> entries.maxByOrNull { it.date } }

            items.mapNotNull { item ->
                val latest = latestByParent[item.id] ?: return@mapNotNull null
                TransactionPreview(
                    name = item.name,
                    category = item.category,
                    type = item.type,
                    amount = latest.value,
                    date = latest.date,
                )
            }.sortedByDescending { it.date }.take(8)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    /**
     * Change in net worth between the two most recent snapshots.
     *
     * Builds a cumulative snapshot per date so a date where only one side changed does not look
     * like a collapse to zero. Returns null when there is no usable history yet.
     */
    val netWorthDelta: StateFlow<Double?> =
        combine(
            balanceSheetDao.getAssetsHistoricalEntries(),
            balanceSheetDao.getLiabilitiesHistoricalEntries(),
        ) { assets, liabilities ->
            val cumulativeValues = { entries: List<com.softklass.annette.data.database.dao.HistoricalEntry>,
                date: Long ->
                entries.groupBy { it.itemId }.values.sumOf { itemEntries ->
                    itemEntries.lastOrNull { it.date <= date }?.value ?: 0.0
                }
            }
            val dates = (assets.map { it.date } + liabilities.map { it.date })
                .distinct()
                .sorted()
            if (dates.size < 2) {
                null
            } else {
                val last = dates.last()
                val previous = dates[dates.size - 2]
                val latestNet = cumulativeValues(assets, last) - cumulativeValues(liabilities, last)
                val previousNet =
                    cumulativeValues(assets, previous) - cumulativeValues(liabilities, previous)
                latestNet - previousNet
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

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
