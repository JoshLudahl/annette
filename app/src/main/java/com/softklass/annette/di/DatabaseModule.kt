package com.softklass.annette.di

import android.content.Context
import androidx.room.Room
import com.softklass.annette.data.database.AnnetteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun providesDatabaseSource(
        @ApplicationContext context: Context,
    ) = Room
        .databaseBuilder(
            context,
            AnnetteDatabase::class.java,
            "annette_database",
        ).build()

    @Singleton
    @Provides
    fun providesAssetDao(database: AnnetteDatabase) = database.assetDao()

    @Singleton
    @Provides
    fun providesLiabilityDao(database: AnnetteDatabase) = database.liabilityDao()

    @Singleton
    @Provides
    fun providesBalanceSheetDao(database: AnnetteDatabase) = database.balanceSheetDao()
}
