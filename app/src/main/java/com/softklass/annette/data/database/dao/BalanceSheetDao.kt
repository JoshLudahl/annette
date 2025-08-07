package com.softklass.annette.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.softklass.annette.data.database.entities.BalanceSheetItem
import kotlinx.coroutines.flow.Flow

@Dao
interface BalanceSheetDao {
    @Query("SELECT * FROM balance_sheet WHERE type = 'liability'")
    fun getLiabilities(): Flow<List<BalanceSheetItem>>

    @Query("SELECT * FROM balance_sheet WHERE type = 'asset'")
    fun getAssets(): Flow<List<BalanceSheetItem>>
}
