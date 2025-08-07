package com.softklass.annette.ui.screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softklass.annette.data.database.dao.AssetDao
import com.softklass.annette.data.database.entities.AssetEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssetsViewModel @Inject constructor(
    private val assetDao: AssetDao
) : ViewModel() {

    private val _assets = MutableStateFlow<List<AssetEntity>>(emptyList())
    val assets: StateFlow<List<AssetEntity>> = _assets.asStateFlow()

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog.asStateFlow()

    init {
        loadAssets()
    }

    private fun loadAssets() {
        viewModelScope.launch {
            assetDao.getAllAssets().collect { assetList ->
                _assets.value = assetList
            }
        }
    }

    fun showAddDialog() {
        _showAddDialog.value = true
    }

    fun hideAddDialog() {
        _showAddDialog.value = false
    }

    fun addAsset(name: String, amount: Double, category: String) {
        viewModelScope.launch {
            val asset = AssetEntity(
                name = name,
                amount = amount,
                category = category
            )
            assetDao.insertAsset(asset)
            hideAddDialog()
        }
    }

    fun deleteAsset(asset: AssetEntity) {
        viewModelScope.launch {
            assetDao.deleteAsset(asset)
        }
    }
}
