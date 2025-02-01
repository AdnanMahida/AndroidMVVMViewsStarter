package com.ad.mvvmstarter.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.mvvmstarter.data.remote.DeviceListItem
import com.ad.mvvmstarter.network.repository.BaseRepository
import com.ad.mvvmstarter.utility.helper.NetworkGeneric
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


abstract class BaseViewModel(private val baseRepository: BaseRepository) : ViewModel() {

    val listOfDevicesLiveData = MutableLiveData<Resource<List<DeviceListItem>>>()

    fun callGetListOfDevices() {
        viewModelScope.launch(Dispatchers.IO) {
            listOfDevicesLiveData.postValue(Resource.Loading())

            val response = baseRepository.callGetListOfDevices { error, message ->
                listOfDevicesLiveData.postValue(Resource.Error(error, message))
            } ?: return@launch

            val listItem =
                NetworkGeneric(response)
                    .getAsList(Array<DeviceListItem>::class.java)
                    .takeIf { it.isNotEmpty() } ?: listOf()

            listOfDevicesLiveData.postValue(Resource.Success(listItem))
        }
    }
}