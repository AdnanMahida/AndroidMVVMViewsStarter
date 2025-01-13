package com.ad.mvvmstarter.core

import androidx.lifecycle.ViewModel
import com.ad.mvvmstarter.network.repository.BaseRepository


abstract class BaseViewModel(private val baseRepository: BaseRepository) : ViewModel() {

}