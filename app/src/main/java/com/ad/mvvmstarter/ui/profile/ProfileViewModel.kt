package com.ad.mvvmstarter.ui.profile

import androidx.lifecycle.ViewModel
import com.ad.mvvmstarter.network.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val repository: BaseRepository
) : ViewModel() {

}