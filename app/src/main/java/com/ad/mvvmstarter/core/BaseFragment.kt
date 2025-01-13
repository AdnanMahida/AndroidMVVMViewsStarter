package com.ad.mvvmstarter.core

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ad.mvvmstarter.preference.AppPreference
import com.ad.mvvmstarter.services.LocationHelper
import com.ad.mvvmstarter.services.LocationService
import com.ad.mvvmstarter.utility.AppConstants
import com.ad.mvvmstarter.utility.dialog.DialogUtils
import com.ad.mvvmstarter.utility.extension.hasLocationPermission
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * This class is parent of all fragment class
 * This class contains all common method of fragment(s).
 */

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {
    private var getBottomBarCallback: GetBottomBarCallback? = null

    @Inject
    lateinit var pref: AppPreference

    /**
     * Used when we don't want to create/inflate view if same fragment instance is used.
     */
    var isFirstTimeLoad: Boolean = true
    var prevViewDataBinding: ViewDataBinding? = null
    fun <T : ViewDataBinding> createOrReloadView(
        inflater: LayoutInflater, resLayout: Int, container: ViewGroup?
    ): T {
        isFirstTimeLoad = false
        if (prevViewDataBinding == null) {
            prevViewDataBinding = DataBindingUtil.inflate(
                inflater, resLayout, container, false
            )
            isFirstTimeLoad = true
        } else if (prevViewDataBinding?.root?.parent != null) {
            container?.removeView(prevViewDataBinding?.root)
        }
        return prevViewDataBinding as T
    }

    open fun attachObserver() {}

    /**
     * ProgressDialogFragment showing methods
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attachObserver()
    }

    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view, savedInstanceState
        )
        Timber.tag("CurrentFragment").i(this.javaClass.simpleName)
        manageBottomBar()
    }

    open fun onBackPressed(): Boolean {
        return false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is GetBottomBarCallback) {
            getBottomBarCallback = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        getBottomBarCallback = null
    }

    fun <T> getParentActivity(): T {
        return requireActivity() as T
    }

    fun showProgressDialog(isShow: Boolean) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showProgressDialog(isShow)
        }
    }

    fun showProgressDialog(isShow: Boolean, title: String) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showProgressDialog(isShow, title)
        }
    }

    fun showUploadProgressDialog(isShow: Boolean, progress: Int = 0) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showUploadProgressDialog(
                isShow = isShow, progress = progress
            )
        }
    }

    fun pushFragment(mFrag: Fragment) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).pushFragment(mFrag)
        }
    }

    fun switchFragment(
        index: Int, clearAllStack: Boolean = false
    ) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).switchFragment(
                index = index, clearAllStack = clearAllStack
            )
        }
    }

    fun popBackStack(
        depth: Int = 0, needsAnimation: Boolean = true, clearStack: Boolean = false
    ) {
        if (activity is BaseActivity) {
//            (activity as AppCompatActivity?)?.hideKeyBoard()
            (activity as BaseActivity).popBackStack(
                depth = depth, needsAnimation = needsAnimation, clearStack = clearStack
            )
        }
    }

    fun showSuccess(message: String?) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showSuccess(message)
        }
    }

    fun showError(message: String?, viewGroup: ViewGroup? = null) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showError(message, viewGroup)
        }
    }

    fun showError(messageId: Int) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showError(messageId)
        }
    }

    /**
     * Without BottomBar
     * */
    fun withoutBottomBar(): BaseFragment {
        var bundle: Bundle? = Bundle()
        if (arguments != null) {
            bundle = arguments
        }

        bundle?.putBoolean(
            AppConstants.EXTRA_WITH_OUT_BOTTOM_BAR, true
        )
        arguments = bundle
        return this
    }

    /**
     * With BottomBar
     * */
    fun withBottomBar(): BaseFragment {
        var bundle: Bundle? = Bundle()
        if (arguments != null) {
            bundle = arguments
        }
        bundle?.putBoolean(
            AppConstants.EXTRA_WITH_OUT_BOTTOM_BAR, false
        )
        arguments = bundle
        return this
    }

    private fun manageBottomBar() {
        val bottomBar = getBottomBarCallback?.getBottomBarView()
        if (arguments != null && arguments?.containsKey(AppConstants.EXTRA_WITH_OUT_BOTTOM_BAR) == true) {
            val withoutBottomBar =
                arguments?.getBoolean(AppConstants.EXTRA_WITH_OUT_BOTTOM_BAR) ?: false
            if (getBottomBarCallback != null) {
                if (withoutBottomBar) {
                    if (bottomBar?.visibility == View.VISIBLE) {
                        bottomBar.visibility = View.GONE
                    }
                } else {
                    if (bottomBar?.visibility == View.GONE) {
                        bottomBar.visibility = View.VISIBLE
                    }
                }
            }
        } else {
            if (getBottomBarCallback != null) {
                if (bottomBar?.visibility == View.GONE) {
                    bottomBar.visibility = View.VISIBLE
                }
            }
        }
    }


    fun isNetworkAvailable(): Boolean {
        if (activity is BaseActivity) {
            return (activity as BaseActivity).isNetworkAvailable()
        }
        return false
    }

    /**
     * Requests the current location.
     */
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    /**
     * Permission launcher using Activity Result API
     * */
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.all { it.value }
            if (allGranted) {
                // Permissions granted, fetch the location
                startLocationTracking()
            } else {
                DialogUtils.showConfirmationDialog(context = requireContext(),
                    title = "",
                    onPositiveCallback = {
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                    },
                    onNegativeCallback = {
                        // "Location permissions are required."
                    })
            }

        }


    fun startLocationTracking() {
        if (!requireContext().hasLocationPermission()) {
            // Request permissions using the launcher
            requestPermissionLauncher.launch(permissions)
            return
        }

        LocationHelper.ifLocationEnable(requireContext()) { isEnabled ->
            if (isEnabled) {
                Intent(requireContext().applicationContext, LocationService::class.java).apply {
                    action = LocationService.ACTION_START
                    requireContext().startService(this)
                }
            }
        }
    }

    fun stopLocationTracking() {
        Intent(requireContext().applicationContext, LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
            requireContext().startService(this)
        }
    }
}
