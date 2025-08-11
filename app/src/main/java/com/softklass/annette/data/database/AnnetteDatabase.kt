package com.softklass.annette.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.softklass.annette.data.database.dao.BalanceSheetDao
import com.softklass.annette.data.database.entities.BalanceSheetItem
import com.softklass.annette.data.database.entities.BalanceSheetValues

@Database(
    entities = [
        BalanceSheetItem::class,
        BalanceSheetValues::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AnnetteDatabase : RoomDatabase() {
    abstract fun balanceSheetDao(): BalanceSheetDao
}
