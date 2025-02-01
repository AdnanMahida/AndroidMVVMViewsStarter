package com.ad.mvvmstarter.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ad.mvvmstarter.R
import com.ad.mvvmstarter.core.BaseFragment
import com.ad.mvvmstarter.core.Resource
import com.ad.mvvmstarter.databinding.FragmentHomeBinding
import timber.log.Timber

class HomeFragment : BaseFragment() {
    companion object {
        const val TAG = "HomeFragment"
        fun getInstance() = HomeFragment()
    }

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = createOrReloadView(inflater, R.layout.fragment_home, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isFirstTimeLoad) {
            initUI()
            callGetDeviceListApi()
        }
    }

    private fun callGetDeviceListApi() {
        viewModel.callGetListOfDevices()
    }

    private fun initUI() {

    }

    override fun attachObserver() {
        viewModel.listOfDevicesLiveData.observe(this) {
            when (it) {
                is Resource.Error -> {
                    Timber.tag(TAG).e("Api Error ${it.errorCode} ${it.errorMessage}")
                }

                is Resource.Loading -> {
                    Timber.tag(TAG).i("Api is Loading...")
                }

                is Resource.Success -> {
                    Timber.tag(TAG).i("Api Success ${it.data.toString()}")
                }
            }
        }
    }
}