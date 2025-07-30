package com.softklass.annette.ui.screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softklass.annette.data.database.dao.LiabilityDao
import com.softklass.annette.data.database.entities.LiabilityEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LiabilitiesViewModel(
    private val liabilityDao: LiabilityDao
) : ViewModel() {

    private val _liabilities = MutableStateFlow<List<LiabilityEntity>>(emptyList())
    val liabilities: StateFlow<List<LiabilityEntity>> = _liabilities.asStateFlow()

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog.asStateFlow()

    init {
        loadLiabilities()
    }

    private fun loadLiabilities() {
        viewModelScope.launch {
            liabilityDao.getAllLiabilities().collect { liabilityList ->
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
            val liability = LiabilityEntity(
                name = name,
                amount = amount,
                category = category
            )
            liabilityDao.insertLiability(liability)
            hideAddDialog()
        }
    }

    fun deleteLiability(liability: LiabilityEntity) {
        viewModelScope.launch {
            liabilityDao.deleteLiability(liability)
        }
    }
}
