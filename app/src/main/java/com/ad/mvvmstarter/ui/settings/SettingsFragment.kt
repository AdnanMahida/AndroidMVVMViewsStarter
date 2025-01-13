package com.ad.mvvmstarter.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ad.mvvmstarter.R
import com.ad.mvvmstarter.core.BaseFragment
import com.ad.mvvmstarter.databinding.FragmentSettingsBinding

class SettingsFragment : BaseFragment() {
    companion object {
        fun getInstance() = SettingsFragment()
    }

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = createOrReloadView(inflater, R.layout.fragment_settings, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isFirstTimeLoad) {
            initUI()
        }
    }

    private fun initUI() {

    }

    override fun attachObserver() {

    }
}