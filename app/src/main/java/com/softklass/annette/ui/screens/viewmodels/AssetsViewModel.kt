package com.softklass.annette.ui.screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softklass.annette.data.database.dao.BalanceSheetDao
import com.softklass.annette.data.database.dao.BalanceSheetItemWithValue
import com.softklass.annette.data.database.dao.HistoricalTotal
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
            balanceSheetDao.getAssetsHistoricalTotals().collect { totals ->
                _historicalTotals.value = totals
            }
        }
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
