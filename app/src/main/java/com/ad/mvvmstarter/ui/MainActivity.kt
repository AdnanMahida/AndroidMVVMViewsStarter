package com.ad.mvvmstarter.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ad.mvvmstarter.R
import com.ad.mvvmstarter.core.BaseActivity
import com.ad.mvvmstarter.core.BaseFragment
import com.ad.mvvmstarter.core.GetBottomBarCallback
import com.ad.mvvmstarter.databinding.ActivityMainBinding
import com.ad.mvvmstarter.ui.home.HomeFragment
import com.ad.mvvmstarter.ui.profile.ProfileFragment
import com.ad.mvvmstarter.ui.settings.SettingsFragment
import com.ad.mvvmstarter.utility.dialog.DialogUtils
import com.ad.mvvmstarter.utility.extension.openAppSystemSettings
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavSwitchController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import com.ncapdevi.fragnav.tabhistory.UniqueTabHistoryStrategy

class MainActivity : BaseActivity(), GetBottomBarCallback, FragNavController.RootFragmentListener {
    companion object {
        fun getIntent(context: Context) = Intent(
            context, MainActivity::class.java
        ).apply {
            flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        private const val INDEX_HOME = 0
        private const val INDEX_PROFILE = 1
        private const val INDEX_SETTINGS = 2
    }

    private var currentTabPosition: Int = INDEX_HOME
    private var mCurrentFragment: Fragment? = null

    private lateinit var binding: ActivityMainBinding

    private val fragNavController: FragNavController by lazy {
        FragNavController(
            supportFragmentManager, R.id.container
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )
        //  setUpWindowInsets(binding.main)

        setupFragNav()
        setBottomBarClickListener()
    }


    private fun setupFragNav() {
        fragNavController.rootFragments = arrayListOf(
            HomeFragment.getInstance(),
            ProfileFragment.getInstance(),
            SettingsFragment.getInstance()
        )

        fragNavController.initialize(
            INDEX_HOME, null
        )

        fragNavController.defaultTransactionOptions = FragNavTransactionOptions.newBuilder().build()
        fragNavController.fragmentHideStrategy = FragNavController.DETACH

        fragNavController.navigationStrategy =
            UniqueTabHistoryStrategy(object : FragNavSwitchController {
                override fun switchTab(
                    index: Int, transactionOptions: FragNavTransactionOptions?
                ) {
                    fragNavController.switchTab(index)
                }
            })

        selectTabAtPosition(INDEX_HOME)
    }

    private fun setBottomBarClickListener() {
        binding.bottomNV.homeLL.setOnClickListener {
            selectBottomTab(INDEX_HOME)
        }
        binding.bottomNV.profileLL.setOnClickListener {
            selectBottomTab(INDEX_PROFILE)
        }
        binding.bottomNV.settingsLL.setOnClickListener {
            selectBottomTab(INDEX_SETTINGS)
        }
    }

    private fun selectBottomTab(newPos: Int) {
        if (currentTabPosition == newPos) {
            fragNavController.clearStack()
            return
        }
        currentTabPosition = newPos
        fragNavController.switchTab(newPos)
        selectTabAtPosition(newPos)
    }

    private fun selectTabAtPosition(index: Int) {
        unSelectBottomBar()
        currentTabPosition = index
        when (index) {
            INDEX_HOME -> {
                binding.bottomNV.homeLL.isSelected = true
            }

            INDEX_PROFILE -> {
                binding.bottomNV.profileLL.isSelected = true
            }

            INDEX_SETTINGS -> {
                binding.bottomNV.settingsLL.isSelected = true
            }
        }
    }

    private fun unSelectBottomBar() {
        binding.bottomNV.homeLL.isSelected = false
        binding.bottomNV.profileLL.isSelected = false
        binding.bottomNV.settingsLL.isSelected = false
    }


    override fun pushFragment(
        mFrag: Fragment, isAdd: Boolean
    ) {
        fragNavController.pushFragment(fragment = mFrag)
        mCurrentFragment = mFrag
    }

    override fun switchFragment(
        index: Int, clearAllStack: Boolean
    ) {
        if (clearAllStack) fragNavController.clearStack()
        selectBottomTab(index)
    }

    private fun getCurrentFragment(): Fragment? {
        return fragNavController.currentFrag
    }

    override fun popBackStack(
        depth: Int, needsAnimation: Boolean, clearStack: Boolean
    ) {
        super.popBackStack(
            depth, needsAnimation, clearStack
        )

        if (depth == 0) {
            fragNavController.popFragment()
        } else {
            fragNavController.popFragments(depth)
        }
    }

    override fun getRootFragment(index: Int): Fragment {
        return when (index) {
            INDEX_HOME -> {
                HomeFragment.getInstance()
            }

            INDEX_PROFILE -> {
                ProfileFragment.getInstance()
            }

            INDEX_SETTINGS -> {
                SettingsFragment.getInstance()
            }

            else -> {
                HomeFragment.getInstance()
            }
        }
    }

    override val numberOfRootFragments: Int get() = 3
    override fun onBackPressed() {
        val currentFragment = mCurrentFragment
        if (currentFragment != null && currentFragment is BaseFragment) {
            if (currentFragment.onBackPressed()) {
                return
            } else {
                if (fragNavController.currentStack != null && fragNavController.currentStack!!.size >= 2) {
                    fragNavController.popFragment()
                    return
                }
            }
        }
        super.onBackPressed()
    }

    override fun getBottomBarView() = binding.bottomNV.root


    /**
     * show push notification permission
     * */
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            pushNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    /**
     * notification permission
     * */
    private val pushNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                DialogUtils.showAlertDialog(
                    this@MainActivity,
                    title = "Notification permission",
                    positiveBtnText = "Open Setting",
                    onPositiveCallback = {
                        openAppSystemSettings()
                    },
                    message = "Enable Notification permission from setting",
                )
            }
        }
}