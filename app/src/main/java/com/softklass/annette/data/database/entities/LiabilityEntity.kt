package com.softklass.annette.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liabilities")
data class LiabilityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val amount: Double,
    val category: String
)