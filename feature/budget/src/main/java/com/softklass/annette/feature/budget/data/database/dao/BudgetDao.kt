package com.softklass.annette.feature.budget.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.softklass.annette.feature.budget.data.database.entities.BudgetItem
import com.softklass.annette.feature.budget.data.database.entities.BudgetValue
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Insert
    suspend fun insertBudgetItem(item: BudgetItem): Long

    @Insert
    suspend fun insertBudgetValue(value: BudgetValue): Long

    @Query("SELECT * FROM budget_item")
    fun getAllBudgetItems(): Flow<List<BudgetItem>>

    @Query("SELECT * FROM budget_item WHERE category = :category")
    fun getBudgetItemsByCategory(category: String): Flow<List<BudgetItem>>

    @Query("SELECT * FROM budget_item WHERE type = :type")
    fun getBudgetItemsByType(type: String): Flow<List<BudgetItem>>

    @Query("SELECT * FROM budget_item WHERE name = :name LIMIT 1")
    suspend fun getBudgetItemByName(name: String): BudgetItem?

    @Query("SELECT * FROM budget_value")
    fun getAllBudgetValues(): Flow<List<BudgetValue>>

    @Query("SELECT * FROM budget_value WHERE parentId = :parentId")
    fun getBudgetValuesByParentId(parentId: Long): Flow<List<BudgetValue>>

    @Query("DELETE FROM budget_value WHERE parentId = :parentId")
    fun deleteAllBudgetValuesByParentId(parentId: Long)

    @Query("DELETE FROM budget_item")
    fun deleteAllBudgetItems()

    @Delete
    suspend fun deleteBudgetItem(item: BudgetItem)

    @Delete
    suspend fun deleteBudgetValue(value: BudgetValue)
}
