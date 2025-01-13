package com.ad.mvvmstarter.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ad.mvvmstarter.utility.AppConstants

@Entity(tableName = AppConstants.TABLE_DEMO)
data class Demo(
    @PrimaryKey
    val id: Int?,
    val name: String?
)