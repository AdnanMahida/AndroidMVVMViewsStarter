package com.ad.mvvmstarter.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ad.mvvmstarter.R
import com.ad.mvvmstarter.core.BaseFragment
import com.ad.mvvmstarter.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment() {
    companion object {
        fun getInstance() = ProfileFragment()
    }

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = createOrReloadView(inflater, R.layout.fragment_profile, container)
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