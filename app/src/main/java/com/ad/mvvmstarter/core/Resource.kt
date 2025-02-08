package com.ad.mvvmstarter.core

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val progress: Int? = null,
    val errorCode: Int? = null,
    val errorMessage: String? = null
) {

    // class in case of success response from api
    class Success<T>(data: T?) : Resource<T>(data = data)

    // class to the UI in case of failure response
    class Error<T>(errorCode: Int, errorMessage: String) :
        Resource<T>(errorCode = errorCode, message = errorMessage)

    // class,to show loading before making an api call
    class Loading<T>(progress: Int? = null) : Resource<T>(progress = progress)
}
