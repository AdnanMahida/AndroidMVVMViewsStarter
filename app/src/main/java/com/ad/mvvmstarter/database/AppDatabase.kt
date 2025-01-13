package com.ad.mvvmstarter.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ad.mvvmstarter.data.local.entity.Demo
import com.ad.mvvmstarter.database.converters.CoreConverters
import com.ad.mvvmstarter.database.dao.DemoDao
import com.ad.mvvmstarter.utility.AppConstants


@Database(
    entities = [
        Demo::class
    ],
    version = AppConstants.DB_VERSION,
    exportSchema = false
)
@TypeConverters(CoreConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDemoDao(): DemoDao
}
