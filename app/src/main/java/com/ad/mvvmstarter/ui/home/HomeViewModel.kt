package com.ad.mvvmstarter.ui.home

import androidx.lifecycle.ViewModel
import com.ad.mvvmstarter.network.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: BaseRepository
) : ViewModel() {

}