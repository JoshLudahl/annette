package com.softklass.annette.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.softklass.annette.data.database.dao.AssetDao
import com.softklass.annette.data.database.dao.BalanceSheetDao
import com.softklass.annette.data.database.dao.LiabilityDao
import com.softklass.annette.data.database.entities.AssetEntity
import com.softklass.annette.data.database.entities.LiabilityEntity

@Database(
    entities = [
        AssetEntity::class,
        LiabilityEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AnnetteDatabase : RoomDatabase() {
    abstract fun assetDao(): AssetDao
    abstract fun liabilityDao(): LiabilityDao
    abstract fun balanceSheetDao(): BalanceSheetDao
}
