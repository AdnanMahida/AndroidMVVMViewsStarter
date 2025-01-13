package com.ad.mvvmstarter.ui.settings

import androidx.lifecycle.ViewModel
import com.ad.mvvmstarter.network.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val repository: BaseRepository
) : ViewModel() {

}