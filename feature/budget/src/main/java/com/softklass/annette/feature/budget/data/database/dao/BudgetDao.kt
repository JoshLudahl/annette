package com.softklass.annette.feature.budget.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.softklass.annette.feature.budget.data.database.entities.BudgetItem
import com.softklass.annette.feature.budget.data.database.entities.BudgetValue

@Dao
interface BudgetDao {
    @Insert
    suspend fun insertBudgetItem(item: BudgetItem): Long

    @Insert
    suspend fun insertBudgetValue(value: BudgetValue): Long
}