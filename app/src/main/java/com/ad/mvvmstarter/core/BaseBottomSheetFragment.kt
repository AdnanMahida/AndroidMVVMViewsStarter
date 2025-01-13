package com.ad.mvvmstarter.core

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ad.mvvmstarter.preference.AppPreference
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * This class is parent of all fragment class
 *
 * This class contains all common method of fragment(s).
 */
@AndroidEntryPoint
abstract class BaseBottomSheetFragment : BottomSheetDialogFragment() {
    @Inject
    lateinit var pref: AppPreference
    private var getBottomBarCallback: GetBottomBarCallback? = null

    /**
     * Used when we dont want to create/inflate view if same fragment instance is used.
     */
    var isFirstTimeLoad: Boolean = true
    var prevViewDataBinding: ViewDataBinding? = null
    fun <T : ViewDataBinding> createOrReloadView(
        inflater: LayoutInflater,
        resLayout: Int,
        container: ViewGroup?
    ): T {
        isFirstTimeLoad = false
        if (prevViewDataBinding == null) {
            prevViewDataBinding = DataBindingUtil.inflate(
                inflater,
                resLayout,
                container,
                false
            )
            isFirstTimeLoad = true
        } else if (prevViewDataBinding?.root?.parent != null) {
            container?.removeView(prevViewDataBinding?.root)
        }
        return prevViewDataBinding as T
    }

    open fun attachObserver() {}
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )
        Timber.tag("CURRENT_FRAG")
            .i(this.javaClass.simpleName)
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

    fun pushFragment(mFrag: Fragment) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).pushFragment(mFrag)
        }
    }

    fun popBackStack(
        depth: Int = 0,
        needsAnimation: Boolean = true,
        clearStack: Boolean = false
    ) {
        if (activity is BaseActivity) {
//			(activity as AppCompatActivity?)?.hideKeyBoard()
            (activity as BaseActivity).popBackStack(
                depth = depth,
                needsAnimation = needsAnimation,
                clearStack = clearStack
            )
        }
    }

    fun switchFragment(index: Int) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).switchFragment(index = index)
        }
    }
}
