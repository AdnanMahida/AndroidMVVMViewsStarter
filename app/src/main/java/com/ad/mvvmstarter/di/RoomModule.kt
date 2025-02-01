package com.ad.mvvmstarter.di

import android.content.Context
import androidx.room.Room
import com.ad.mvvmstarter.database.AppDatabase
import com.ad.mvvmstarter.database.dao.DemoDao
import com.ad.mvvmstarter.utility.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * RoomModule used for
 * room db related DI
 * */
@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    // Provide Room Database
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppConstants.DB_NAME
        ).build()
    }


    // Provide DAO
    @Provides
    @Singleton
    fun provideHomeDao(appDatabase: AppDatabase): DemoDao {
        return appDatabase.getDemoDao()
    }
}
