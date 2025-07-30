package com.softklass.annette.data.database.dao

import androidx.room.*
import com.softklass.annette.data.database.entities.LiabilityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LiabilityDao {
    @Query("SELECT * FROM liabilities ORDER BY name ASC")
    fun getAllLiabilities(): Flow<List<LiabilityEntity>>

    @Query("SELECT * FROM liabilities WHERE id = :id")
    suspend fun getLiabilityById(id: Long): LiabilityEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLiability(liability: LiabilityEntity): Long

    @Update
    suspend fun updateLiability(liability: LiabilityEntity)

    @Delete
    suspend fun deleteLiability(liability: LiabilityEntity)

    @Query("DELETE FROM liabilities WHERE id = :id")
    suspend fun deleteLiabilityById(id: Long)
}