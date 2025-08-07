package com.softklass.annette.ui.screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softklass.annette.data.database.dao.BalanceSheetDao
import com.softklass.annette.data.database.dao.BalanceSheetItemWithValue
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

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog.asStateFlow()

    init {
        loadLiabilities()
    }

    private fun loadLiabilities() {
        viewModelScope.launch {
            balanceSheetDao.getLiabilitiesWithLatestValues().collect { liabilityList ->
                _liabilities.value = liabilityList
            }
        }
    }

    fun showAddDialog() {
        _showAddDialog.value = true
    }

    fun hideAddDialog() {
        _showAddDialog.value = false
    }

    fun addLiability(name: String, amount: Double, category: String) {
        viewModelScope.launch {
            val liability = BalanceSheetItem(
                name = name,
                category = category,
                type = "liability"
            )
            balanceSheetDao.insertBalanceSheetItemWithValue(liability, amount)
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
