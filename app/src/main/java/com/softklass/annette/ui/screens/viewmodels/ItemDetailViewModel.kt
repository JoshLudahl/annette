package com.softklass.annette.ui.screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softklass.annette.data.database.dao.BalanceSheetDao
import com.softklass.annette.data.database.entities.BalanceSheetItem
import com.softklass.annette.data.database.entities.BalanceSheetValues
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val balanceSheetDao: BalanceSheetDao
) : ViewModel() {

    private val _item = MutableStateFlow<BalanceSheetItem?>(null)
    val item: StateFlow<BalanceSheetItem?> = _item.asStateFlow()

    private val _historicalValues = MutableStateFlow<List<BalanceSheetValues>>(emptyList())
    val historicalValues: StateFlow<List<BalanceSheetValues>> = _historicalValues.asStateFlow()

    private val _showAddValueDialog = MutableStateFlow(false)
    val showAddValueDialog: StateFlow<Boolean> = _showAddValueDialog.asStateFlow()

    private val _showDeleteDialog = MutableStateFlow<BalanceSheetValues?>(null)
    val showDeleteDialog: StateFlow<BalanceSheetValues?> = _showDeleteDialog.asStateFlow()

    fun loadItemDetails(itemId: Long, itemName: String, itemCategory: String, itemType: String) {
        _item.value = BalanceSheetItem(
            id = itemId,
            name = itemName,
            category = itemCategory,
            type = itemType
        )
        loadHistoricalValues(itemId)
    }

    private fun loadHistoricalValues(itemId: Long) {
        viewModelScope.launch {
            balanceSheetDao.getBalanceSheetValues(itemId).collect { values ->
                _historicalValues.value = values
            }
        }
    }

    fun showAddValueDialog() {
        _showAddValueDialog.value = true
    }

    fun hideAddValueDialog() {
        _showAddValueDialog.value = false
    }

    fun addValue(value: Double, date: Long) {
        viewModelScope.launch {
            _item.value?.let { item ->
                val balanceSheetValue = BalanceSheetValues(
                    parentId = item.id,
                    value = value,
                    date = date
                )
                balanceSheetDao.insertBalanceSheetValue(balanceSheetValue)
                hideAddValueDialog()
            }
        }
    }

    fun showDeleteDialog(value: BalanceSheetValues) {
        _showDeleteDialog.value = value
    }

    fun hideDeleteDialog() {
        _showDeleteDialog.value = null
    }

    fun deleteValue(value: BalanceSheetValues) {
        viewModelScope.launch {
            balanceSheetDao.deleteBalanceSheetValue(value)
            hideDeleteDialog()
        }
    }

    fun updateItemDetails(name: String, category: String) {
        viewModelScope.launch {
            _item.value?.let { item ->
                val updatedItem = item.copy(name = name, category = category)
                balanceSheetDao.updateBalanceSheetItem(updatedItem)
                _item.value = updatedItem
            }
        }
    }
}