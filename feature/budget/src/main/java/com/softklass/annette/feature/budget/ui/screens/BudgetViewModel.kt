package com.softklass.annette.feature.budget.ui.screens

import androidx.lifecycle.ViewModel
import com.softklass.annette.feature.budget.data.database.dao.BudgetDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetDao: BudgetDao
) : ViewModel() {

}