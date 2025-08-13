package com.softklass.annette.ui.screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softklass.annette.data.database.dao.BalanceSheetDao
import com.softklass.annette.data.database.dao.BalanceSheetItemWithValue
import com.softklass.annette.data.database.dao.HistoricalEntry
import com.softklass.annette.data.database.dao.HistoricalTotal
import com.softklass.annette.data.database.entities.BalanceSheetItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiabilitiesViewModel @Inject constructor(
    private val balanceSheetDao: BalanceSheetDao
) : ViewModel() {

    private val _liabilities = MutableStateFlow<List<BalanceSheetItemWithValue>>(emptyList())
    val liabilities: StateFlow<List<BalanceSheetItemWithValue>> = _liabilities.asStateFlow()

    private val _historicalTotals = MutableStateFlow<List<HistoricalTotal>>(emptyList())
    val historicalTotals: StateFlow<List<HistoricalTotal>> = _historicalTotals.asStateFlow()

    private val _showChart = MutableStateFlow(false)
    val showChart: StateFlow<Boolean> = _showChart.asStateFlow()

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog.asStateFlow()

    init {
        loadLiabilities()
        loadHistoricalTotals()
    }

    private fun loadLiabilities() {
        viewModelScope.launch {
            balanceSheetDao.getLiabilitiesWithLatestValues().collect { liabilityList ->
                _liabilities.value = liabilityList
            }
        }
    }

    private fun loadHistoricalTotals() {
        viewModelScope.launch {
            balanceSheetDao.getLiabilitiesHistoricalEntries().collect { entries ->
                _historicalTotals.value = computeForwardFilledTotals(entries)
            }
        }
    }

    private fun computeForwardFilledTotals(entries: List<HistoricalEntry>): List<HistoricalTotal> {
        if (entries.isEmpty()) return emptyList()

        val dates = entries.map { it.date }.toSortedSet().toList()

        val itemDateValueMap: Map<Long, Map<Long, Double>> = entries
            .groupBy { it.itemId }
            .mapValues { (_, list) ->
                list
                    .groupBy { it.date }
                    .mapValues { (_, sameDateList) -> sameDateList.maxByOrNull { e -> e.date }!!.value }
            }

        val lastValues = mutableMapOf<Long, Double?>()
        val result = mutableListOf<HistoricalTotal>()

        for (date in dates) {
            var sum = 0.0
            for ((itemId, dateMap) in itemDateValueMap) {
                val v = dateMap[date]
                if (v != null) {
                    lastValues[itemId] = v
                }
                val current = lastValues[itemId]
                if (current != null) {
                    sum += current
                }
            }
            result += HistoricalTotal(date = date, totalValue = sum)
        }
        return result
    }

    fun toggleChart() {
        _showChart.value = !_showChart.value
    }

    fun showAddDialog() {
        _showAddDialog.value = true
    }

    fun hideAddDialog() {
        _showAddDialog.value = false
    }

    fun addLiability(name: String, amount: Double, category: String, date: Long) {
        viewModelScope.launch {
            val liability = BalanceSheetItem(
                name = name,
                category = category,
                type = "liability",
            )
            balanceSheetDao.insertBalanceSheetItemWithValue(liability, amount, date)
            hideAddDialog()
        }
    }

    fun deleteLiability(liability: BalanceSheetItemWithValue) {
        viewModelScope.launch {
            val balanceSheetItem = BalanceSheetItem(
                id = liability.id,
                name = liability.name,
                category = liability.category,
                type = liability.type
            )
            balanceSheetDao.deleteBalanceSheetItemWithValues(balanceSheetItem)
        }
    }
}
