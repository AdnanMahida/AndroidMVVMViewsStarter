package com.ad.mvvmstarter.workmanagers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.ad.mvvmstarter.network.repository.BaseRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Work manager for background works
 * */
@HiltWorker
class DemoSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: BaseRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        val id = inputData.getInt(ID_KEY, 0)

        repository.callDemoBackgroundSyncApi(id)
        // handle api

        Result.success()
    }

    companion object {
        const val ID_KEY = "id"

        fun enqueueWork(context: Context, id: Int): OneTimeWorkRequest {
            val workManager = WorkManager.getInstance(context)

            val inputData = Data.Builder().putInt(ID_KEY, id).build()

            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

            val workRequest = OneTimeWorkRequestBuilder<DemoSyncWorker>().setInputData(inputData)
                .setConstraints(constraints).build()

            workManager.enqueue(workRequest)
            return workRequest
        }
    }
}