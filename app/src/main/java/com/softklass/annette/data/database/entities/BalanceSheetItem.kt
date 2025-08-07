package com.softklass.annette.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "balance_sheet")
data class BalanceSheetItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String,
    val type: String,
)

@Entity(tableName = "balance_sheet_values")
data class BalanceSheetValues(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val parentId: Long,
    val value: Double,
    val date: Long = System.currentTimeMillis(),
)
