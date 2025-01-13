package com.ad.mvvmstarter.utility.helper

import com.google.gson.GsonBuilder


/**
 * Used for convert object to string and
 * string to objects
 * */

private val gson = GsonBuilder().create()


fun <T> getObjectFromJsonString(
    jsonData: String,
    modelClass: Class<*>
): Any? {
    return gson.fromJson(
        jsonData,
        modelClass
    )
}

fun getJsonStringFromObject(modelClass: Any): String {
    return gson.toJson(modelClass)
}
