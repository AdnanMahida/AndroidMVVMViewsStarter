package com.ad.mvvmstarter.core

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.ad.mvvmstarter.preference.AppPreference
import com.ad.mvvmstarter.progress.ProgressBarDialog
import com.ad.mvvmstarter.utility.AppConstants
import com.ad.mvvmstarter.utility.extension.hideKeyboard
import com.ad.mvvmstarter.utility.extension.showSnackBar
import com.ad.mvvmstarter.utility.helper.NetworkConnectionLiveData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Base Activity class is parent of all other activities
 * This class contains all the common functions of child activities
 */

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {
    /**
     * ProgressDialogFragment showing methods
     */
    private val progressDialog = ProgressBarDialog()

    @Inject
    lateinit var pref: AppPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.BLACK),
            navigationBarStyle = SystemBarStyle.dark(Color.BLACK)
        )
        monitorNetworkConnection()
        attachObserver()
    }

    private fun monitorNetworkConnection() {
        val networkConnectionLiveData = NetworkConnectionLiveData(applicationContext)
        networkConnectionLiveData.observe(this@BaseActivity) { isConnected ->
            // monitor app connection
            // isConnected
        }
    }

    /**
     * Show progress dialog
     * */
    fun showProgressDialog(isShow: Boolean) {
        if (isShow) {
            if (!progressDialog.isShowing()) {
                progressDialog.show(this@BaseActivity)
            }
        } else {
            progressDialog.dismiss()
        }
    }


    /**
     * Show progress dialog with title
     * */
    fun showProgressDialog(isShow: Boolean, title: String) {
        if (isShow) {
            if (!progressDialog.isShowing()) {
                progressDialog.show(this@BaseActivity, title)
            }
        } else {
            progressDialog.dismiss()
        }
    }

    /**
     * Show progress dialog
     * */
    fun showUploadProgressDialog(isShow: Boolean, progress: Int = 0) {
        if (isShow) {
            if (!progressDialog.isShowing()) {
                progressDialog.show(this@BaseActivity)
            }
            progressDialog.setProgress(progress)
        } else {
            progressDialog.dismiss()
        }
    }


    protected fun setUpWindowInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    /**
     * Used for monitor observers with live data or flow
     * */
    open fun attachObserver() {}

    /**
     * push fragment transaction between fragments
     * */
    open fun pushFragment(
        mFrag: Fragment,
        isAdd: Boolean = true
    ) {
    }

    /**
     * Switch between two fragment
     * */
    open fun switchFragment(
        index: Int,
        clearAllStack: Boolean = false
    ) {
    }

    /**
     * Remove fragment
     * */
    open fun popBackStack(
        depth: Int = 0,
        needsAnimation: Boolean = true,
        clearStack: Boolean = false
    ) {
    }

    /**
     * Handling break press
     * */
    override fun onBackPressed() {
        var isHandled = false
        for (fragment in supportFragmentManager.fragments) {
            if (fragment is BaseFragment) {
                isHandled = fragment.onBackPressed()
                if (isHandled) {
                    break
                }
            }
        }
        if (!isHandled) {
            super.onBackPressed()
        }
    }

    /**
     * show success snack bar
     * @param message message string
     * */
    fun showSuccess(message: String?) {
        showSnackBar(
            message = message,
            snackBarType = AppConstants.SnackBarType.SUCCESS
        )
    }

    /**
     * show error snack bar
     * @param message message string
     * @param viewGroup view that need to display
     * */
    fun showError(message: String?, viewGroup: ViewGroup? = null) {
        showSnackBar(
            message = message,
            snackBarType = AppConstants.SnackBarType.ERROR,
            viewGroup = viewGroup
        )
    }

    fun showError(messageId: Int) {
        showSnackBar(
            message = getString(messageId),
            snackBarType = AppConstants.SnackBarType.ERROR
        )
    }

    fun hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }
}