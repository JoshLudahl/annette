package com.softklass.annette.ui.screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softklass.annette.data.database.dao.AssetDao
import com.softklass.annette.data.database.dao.LiabilityDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetWorthViewModel @Inject constructor(
    private val assetDao: AssetDao,
    private val liabilityDao: LiabilityDao
) : ViewModel() {

    private val _totalAssets = MutableStateFlow(0.0)
    val totalAssets: StateFlow<Double> = _totalAssets.asStateFlow()
    
    private val _totalLiabilities = MutableStateFlow(0.0)
    val totalLiabilities: StateFlow<Double> = _totalLiabilities.asStateFlow()
    
    private val _netWorth = MutableStateFlow(0.0)
    val netWorth: StateFlow<Double> = _netWorth.asStateFlow()

    init {
        loadTotalAssets()
        loadTotalLiabilities()
        calculateNetWorth()
    }

    private fun loadTotalAssets() {
        viewModelScope.launch {
            assetDao.getAllAssets().collect { assets ->
                _totalAssets.value = assets.sumOf { it.amount }
            }
        }
    }
    
    private fun loadTotalLiabilities() {
        viewModelScope.launch {
            liabilityDao.getAllLiabilities().collect { liabilities ->
                _totalLiabilities.value = liabilities.sumOf { it.amount }
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