package com.ad.mvvmstarter.preference

import android.content.Context
import android.content.SharedPreferences
import com.ad.mvvmstarter.data.model.User
import com.ad.mvvmstarter.utility.helper.getJsonStringFromObject
import com.ad.mvvmstarter.utility.helper.getObjectFromJsonString
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This class is used for storing and retrieving shared preference values.
 */
@Singleton
class AppPreference @Inject constructor(
    @ApplicationContext
    val context: Context
) {
    companion object {
        private const val PREF_USER_PREF = "UserPref"

        private const val DEVICE_TOKEN = "deviceToken"
        private const val ACCESS_TOKEN = "accessToken"
        private const val IS_LOGIN = "isLogin"
        private const val USER_PROFILE = "userProfile"
    }

    private val userPreferences: SharedPreferences =
        context.getSharedPreferences(
            PREF_USER_PREF,
            Context.MODE_PRIVATE
        )


    var deviceToken: String?
        get() = userPreferences.getString(
            DEVICE_TOKEN,
            ""
        )
        set(value) = userPreferences.edit()
            .putString(
                DEVICE_TOKEN,
                value
            )
            .apply()
    var accessToken: String?
        get() = userPreferences.getString(
            ACCESS_TOKEN,
            ""
        )
        set(value) = userPreferences.edit()
            .putString(
                ACCESS_TOKEN,
                value
            )
            .apply()
    var isLogin: Boolean
        get() = userPreferences.getBoolean(
            IS_LOGIN,
            false
        )
        set(value) = userPreferences.edit()
            .putBoolean(
                IS_LOGIN,
                value
            )
            .apply()

    /**
     * Used to clear SharedPreferences.
     *
     * @return String
     */
    fun clearUserPreference() {
        userPreferences.edit()
            .clear()
            .apply()
        userPreferences.edit()
            .putBoolean(
                IS_LOGIN,
                false
            )
            .apply()
    }

    /**
     * Store and get logged in user profile details
     */
    var userData: User?
        get() = getObjectFromJsonString<User>(
            userPreferences.getString(USER_PROFILE, "") ?: "",
            User::class.java
        ) as User?
        set(value) =
            if (value == null) {
                userPreferences.edit()
                    .putString(
                        USER_PROFILE,
                        ""
                    )
                    .apply()
            } else {
                userPreferences.edit()
                    .putString(
                        USER_PROFILE,
                        getJsonStringFromObject(value)
                    )
                    .apply()
            }


}