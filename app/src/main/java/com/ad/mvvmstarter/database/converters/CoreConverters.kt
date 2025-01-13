package com.ad.mvvmstarter.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Room Database Converters
 * */
class CoreConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromList(value: List<Int>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toList(value: String): List<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromAny(value: Any?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toAny(value: String?): Any? {
        return value?.let {
            gson.fromJson(it, Any::class.java)
        }
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.let {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(value, type)
        }
    }
}
