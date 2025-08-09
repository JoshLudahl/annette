package com.softklass.annette.ui.screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softklass.annette.data.database.dao.BalanceSheetDao
import com.softklass.annette.data.database.dao.BalanceSheetItemWithValue
import com.softklass.annette.data.database.dao.HistoricalTotal
import com.softklass.annette.data.database.dao.HistoricalEntry
import com.softklass.annette.data.database.entities.BalanceSheetItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssetsViewModel @Inject constructor(
    private val balanceSheetDao: BalanceSheetDao
) : ViewModel() {

    private val _assets = MutableStateFlow<List<BalanceSheetItemWithValue>>(emptyList())
    val assets: StateFlow<List<BalanceSheetItemWithValue>> = _assets.asStateFlow()

    private val _historicalTotals = MutableStateFlow<List<HistoricalTotal>>(emptyList())
    val historicalTotals: StateFlow<List<HistoricalTotal>> = _historicalTotals.asStateFlow()

    private val _showChart = MutableStateFlow(false)
    val showChart: StateFlow<Boolean> = _showChart.asStateFlow()

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog.asStateFlow()

    init {
        loadAssets()
        loadHistoricalTotals()
    }

    private fun loadAssets() {
        viewModelScope.launch {
            balanceSheetDao.getAssetsWithLatestValues().collect { assetList ->
                _assets.value = assetList
            }
        }
    }

    private fun loadHistoricalTotals() {
        viewModelScope.launch {
            balanceSheetDao.getAssetsHistoricalEntries().collect { entries ->
                _historicalTotals.value = computeForwardFilledTotals(entries)
            }
        }
    }

    private fun computeForwardFilledTotals(entries: List<HistoricalEntry>): List<HistoricalTotal> {
        if (entries.isEmpty()) return emptyList()

        // Distinct dates across all entries, ascending
        val dates = entries.map { it.date }.toSortedSet().toList()

        // For each item, map of date -> latest value at that date (if multiple within same date timestamp, last by timestamp wins)
        val itemDateValueMap: Map<Long, Map<Long, Double>> = entries
            .groupBy { it.itemId }
            .mapValues { (_, list) ->
                list
                    .groupBy { it.date }
                    .mapValues { (_, sameDateList) -> sameDateList.maxByOrNull { e -> e.date }!!.value }
            }

        // Maintain last known value per item while iterating dates
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

    fun addAsset(name: String, amount: Double, category: String) {
        viewModelScope.launch {
            val asset = BalanceSheetItem(
                name = name,
                category = category,
                type = "asset"
            )
            balanceSheetDao.insertBalanceSheetItemWithValue(asset, amount)
            hideAddDialog()
        }
    }

    fun deleteAsset(asset: BalanceSheetItemWithValue) {
        viewModelScope.launch {
            val balanceSheetItem = BalanceSheetItem(
                id = asset.id,
                name = asset.name,
                category = asset.category,
                type = asset.type
            )
            balanceSheetDao.deleteBalanceSheetItemWithValues(balanceSheetItem)
        }
    }
}
