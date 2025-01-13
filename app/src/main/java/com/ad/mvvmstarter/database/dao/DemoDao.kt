package com.ad.mvvmstarter.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ad.mvvmstarter.data.local.entity.Demo
import com.ad.mvvmstarter.utility.AppConstants

@Dao
interface DemoDao {

    /**
     * Demo entity
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDemo(data: List<Demo>)

    @Query("SELECT * FROM ${AppConstants.TABLE_DEMO}")
    suspend fun getAllDemoList(): List<Demo>?

    @Query("DELETE FROM ${AppConstants.TABLE_DEMO}")
    suspend fun clearAllDemo()

}