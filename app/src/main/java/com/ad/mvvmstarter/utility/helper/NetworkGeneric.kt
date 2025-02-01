package com.ad.mvvmstarter.utility.helper

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

class NetworkGeneric(private val jsonElement: JsonElement?) {

    fun with(jsonElement: JsonElement?): NetworkGeneric {
        return NetworkGeneric(jsonElement)
    }

    fun <T> getAsList(clazz: Class<Array<T>>): List<T> {
        if (jsonElement is JsonArray) {
            val jsonToObject = Gson().fromJson(jsonElement, clazz)
            return listOf(*jsonToObject)
        } else if (jsonElement is JsonObject) {
            val jsonToObject = Gson().fromJson(jsonElement, clazz.componentType) as T
            return listOf(jsonToObject)
        }
        return ArrayList()
    }

    fun <T> getAsObject(clazz: Class<T>?): T? {
        if (jsonElement is JsonArray) {
            return Gson().fromJson(jsonElement.getAsJsonArray()[0], clazz)
        } else if (jsonElement is JsonObject) {
            return Gson().fromJson(jsonElement, clazz)
        }
        return null
    }

    fun getAsObject(): JsonObject? {
        if (jsonElement is JsonObject) {
            return jsonElement.getAsJsonObject()
        }
        return null
    }
}