package com.softklass.annette.feature.budget.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.softklass.annette.feature.budget.data.model.BudgetItemType

@Entity(tableName = "budget_item")
data class BudgetItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String,
    val type: BudgetItemType,
    val dueDate: Long = System.currentTimeMillis(),
)

@Entity(tableName = "budget_value")
data class BudgetValue(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val parentId: Long,
    val value: Double,
    val date: Long = System.currentTimeMillis(),
)
