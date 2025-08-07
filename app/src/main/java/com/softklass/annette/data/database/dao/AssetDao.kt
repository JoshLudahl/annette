package com.softklass.annette.data.database.dao

import androidx.room.*
import com.softklass.annette.data.database.entities.AssetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {
    @Query("SELECT * FROM assets ORDER BY name ASC")
    fun getAllAssets(): Flow<List<AssetEntity>>

    @Query("SELECT * FROM assets WHERE id = :id")
    suspend fun getAssetById(id: Long): AssetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsset(asset: AssetEntity): Long

    @Update
    suspend fun updateAsset(asset: AssetEntity)

    @Delete
    suspend fun deleteAsset(asset: AssetEntity)

    @Query("DELETE FROM assets WHERE id = :id")
    suspend fun deleteAssetById(id: Long)

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM assets")
    fun getTotalAssetsValue(): Flow<Double>
}