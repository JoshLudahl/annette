package com.softklass.annette.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.softklass.annette.data.database.entities.BalanceSheetItem
import com.softklass.annette.data.database.entities.BalanceSheetValues
import kotlinx.coroutines.flow.Flow

@Dao
interface BalanceSheetDao {
    @Query("SELECT * FROM balance_sheet WHERE type = 'liability'")
    fun getLiabilities(): Flow<List<BalanceSheetItem>>

    @Query("SELECT * FROM balance_sheet WHERE type = 'asset'")
    fun getAssets(): Flow<List<BalanceSheetItem>>

    @Insert
    suspend fun insertBalanceSheetItem(item: BalanceSheetItem): Long

    @Insert
    suspend fun insertBalanceSheetValue(value: BalanceSheetValues)

    @Update
    suspend fun updateBalanceSheetItem(item: BalanceSheetItem)

    @Delete
    suspend fun deleteBalanceSheetItem(item: BalanceSheetItem)

    @Query("DELETE FROM balance_sheet_values WHERE parentId = :itemId")
    suspend fun deleteBalanceSheetValues(itemId: Long)

    @Query("SELECT * FROM balance_sheet_values WHERE parentId = :itemId ORDER BY date DESC")
    fun getBalanceSheetValues(itemId: Long): Flow<List<BalanceSheetValues>>

    @Query("SELECT * FROM balance_sheet_values WHERE parentId = :itemId ORDER BY date DESC LIMIT 1")
    suspend fun getLatestBalanceSheetValue(itemId: Long): BalanceSheetValues?

    @Transaction
    suspend fun insertBalanceSheetItemWithValue(item: BalanceSheetItem, amount: Double): Long {
        val itemId = insertBalanceSheetItem(item)
        val value = BalanceSheetValues(
            parentId = itemId,
            value = amount
        )
        insertBalanceSheetValue(value)
        return itemId
    }

    @Transaction
    suspend fun deleteBalanceSheetItemWithValues(item: BalanceSheetItem) {
        deleteBalanceSheetValues(item.id)
        deleteBalanceSheetItem(item)
    }

    @Query("""
        SELECT bs.*, bsv.value 
        FROM balance_sheet bs 
        LEFT JOIN balance_sheet_values bsv ON bs.id = bsv.parentId 
        WHERE bs.type = 'asset' AND (bsv.date = (
            SELECT MAX(date) FROM balance_sheet_values WHERE parentId = bs.id
        ) OR bsv.date IS NULL)
    """)
    fun getAssetsWithLatestValues(): Flow<List<BalanceSheetItemWithValue>>

    @Query("""
        SELECT bs.*, bsv.value 
        FROM balance_sheet bs 
        LEFT JOIN balance_sheet_values bsv ON bs.id = bsv.parentId 
        WHERE bs.type = 'liability' AND (bsv.date = (
            SELECT MAX(date) FROM balance_sheet_values WHERE parentId = bs.id
        ) OR bsv.date IS NULL)
    """)
    fun getLiabilitiesWithLatestValues(): Flow<List<BalanceSheetItemWithValue>>
}

data class BalanceSheetItemWithValue(
    val id: Long,
    val name: String,
    val category: String,
    val type: String,
    val value: Double?
)
