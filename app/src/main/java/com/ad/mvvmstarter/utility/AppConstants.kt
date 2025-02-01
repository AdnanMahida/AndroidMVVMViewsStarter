package com.ad.mvvmstarter.utility

import com.ad.mvvmstarter.R

object AppConstants {
    /**
     * Network constants
     * */
    const val CONNECTION_TIMEOUT: Long = 180
    const val READ_TIMEOUT: Long = 120
    const val WRITE_TIMEOUT: Long = 120

    const val AUTHENTICATION_ERROR_CODE = 401
    const val BAD_GATEWAY_ERROR_CODE = 502
    const val NETWORK_ERROR_CODE = 1001

    const val NETWORK_MESSAGE =
        "It seems your internet is not available, please check it and try again later."
    const val SESSION_EXPIRED = "Session Expired"
    const val TIMEOUT_MESSAGE = "Socket Timeout"
    const val SOMETHING_WENT_WRONG = "Something went wrong"


    /**
     * core constants
     * */
    const val EXTRA_WITH_OUT_BOTTOM_BAR = "withOutBottomBar"

    /*
    * Snack Bar types
    * */
    const val SNACK_BAR_TYPE_ERROR = 1
    const val SNACK_BAR_TYPE_SUCCESS = 2
    const val SNACK_BAR_TYPE_MESSAGE = 3

    const val ANDROID = "Android"
    const val AUTH_TOKEN = "Authorization"


    /**
     * DB Nam
     * */
    const val DB_NAME = "DemoDB"
    const val DB_VERSION = 1

    /**
     * Local Table Names
     * */
    const val TABLE_DEMO = "Demo" // for demo


    /**
     * Snack-bar status types
     * */
    enum class SnackBarType {
        SUCCESS {
            override fun getBackgroundColorRes(): Int {
                return R.color.snack_bar_success_bg
            }

            override fun getTextColorRes(): Int {
                return R.color.snack_bar_success_txt
            }
        },
        ERROR {
            override fun getBackgroundColorRes(): Int {
                return R.color.snack_bar_error_bg
            }

            override fun getTextColorRes(): Int {
                return R.color.snack_bar_error_txt
            }
        },
        WARNING {
            override fun getBackgroundColorRes(): Int {
                return R.color.snack_bar_warning_bg
            }

            override fun getTextColorRes(): Int {
                return R.color.snack_bar_warning_text
            }
        };

        abstract fun getBackgroundColorRes(): Int
        abstract fun getTextColorRes(): Int
    }

}