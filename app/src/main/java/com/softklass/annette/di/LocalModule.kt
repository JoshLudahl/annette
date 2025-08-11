package com.softklass.annette.di

import android.content.Context
import com.softklass.annette.data.preferences.SettingsPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideSettingsPreferences(
        @ApplicationContext context: Context
    ): SettingsPreferences = SettingsPreferences(context)
}
