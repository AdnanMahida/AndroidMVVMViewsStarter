package com.ad.mvvmstarter.di

import com.ad.mvvmstarter.network.repository.BaseRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface LocationServiceEntryPoint {
    fun provideBaseRepository(): BaseRepository
}