package com.ad.mvvmstarter.network

import com.ad.mvvmstarter.utility.AppConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 * Function that executes the given function on Dispatchers.IO context and switch to Dispatchers.Main context when an error occurs
 * @param callFunction is the function that is returning the wanted object. It must be a suspend function. Eg:
 * override suspend fun loginUser(body: LoginUserBody, emitter: RemoteErrorEmitter): LoginUserResponse?  = safeApiCall( { authApi.loginUser(body)} , emitter)
 * @param onError is the interface that handles the error messages. The error messages must be displayed on the MainThread, or else they would throw an Exception.
 */

suspend inline fun <T> safeApiCall(
    crossinline onError: (statusCode: Int, message: String) -> Unit,
    crossinline callFunction: suspend () -> T
): T? {
    return try {
        val myObject = withContext(Dispatchers.IO) { callFunction.invoke() }
        myObject
    } catch (e: Exception) {
        e.printStackTrace()
        when (e) {
            is HttpException -> {
                if (e.code() == 401) {
                    onError.invoke(
                        AppConstants.AUTHENTICATION_ERROR_CODE,
                        AppConstants.SESSION_EXPIRED
                    )
                } else if (e.code() == 502) {
                    onError.invoke(
                        AppConstants.AUTHENTICATION_ERROR_CODE,
                        AppConstants.SESSION_EXPIRED
                    )
                } else {
                    onError.invoke(e.code(), e.response()?.errorBody()?.toString() ?: "")
                }
            }

            else -> {
                withContext(Dispatchers.Main) {
                    val errorPair = getErrorSettings(e)
                    onError.invoke(errorPair.first, errorPair.second)
                }
            }
        }

        null
    }
}

fun getErrorSettings(e: Exception): Pair<Int, String> {
    return when (e) {
        is SocketTimeoutException -> {
            Pair(AppConstants.BAD_GATEWAY_ERROR_CODE, AppConstants.TIMEOUT_MESSAGE)
        }

        is ConnectException -> {
            Pair(AppConstants.BAD_GATEWAY_ERROR_CODE, AppConstants.SOMETHING_WENT_WRONG)
        }

        is UnknownHostException -> {
            Pair(AppConstants.BAD_GATEWAY_ERROR_CODE, AppConstants.SOMETHING_WENT_WRONG)
        }

        is SSLHandshakeException -> {
            Pair(AppConstants.BAD_GATEWAY_ERROR_CODE, AppConstants.SOMETHING_WENT_WRONG)
        }

        else -> {
            Pair(0, AppConstants.SOMETHING_WENT_WRONG)
        }
    }
}