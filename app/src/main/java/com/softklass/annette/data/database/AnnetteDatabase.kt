package com.softklass.annette.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.softklass.annette.data.database.dao.BalanceSheetDao
import com.softklass.annette.data.database.entities.BalanceSheetItem
import com.softklass.annette.data.database.entities.BalanceSheetValues
import com.softklass.annette.feature.budget.data.database.dao.BudgetDao
import com.softklass.annette.feature.budget.data.database.entities.BudgetItem
import com.softklass.annette.feature.budget.data.database.entities.BudgetValue

@Database(
    entities = [
        BalanceSheetItem::class,
        BalanceSheetValues::class,
        BudgetItem::class,
        BudgetValue::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AnnetteDatabase : RoomDatabase() {
    abstract fun balanceSheetDao(): BalanceSheetDao
    abstract fun budgetDao(): BudgetDao
}
