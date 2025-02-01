package com.ad.mvvmstarter.ui.home

import com.ad.mvvmstarter.core.BaseViewModel
import com.ad.mvvmstarter.network.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    baseRepository: BaseRepository,
    val repository: BaseRepository
) : BaseViewModel(baseRepository) {

}