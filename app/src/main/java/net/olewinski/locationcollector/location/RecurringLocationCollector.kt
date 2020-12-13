package net.olewinski.locationcollector.location

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import net.olewinski.locationcollector.data.models.LocationData
import net.olewinski.locationcollector.data.repository.LocationRepository
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

private const val FETCH_LOCATION_TIMEOUT_MILLISECONDS = 60000L
private const val SAVE_LOCATION_WORK_TAG = "SAVE_LOCATION_WORK_TAG"
private const val SAVE_LOCATION_WORK_INTERVAL_TIME_VALUE = 15L
private val SAVE_LOCATION_WORK_INTERVAL_TIME_UNIT = TimeUnit.MINUTES

class RecurringLocationCollector(private val workManager: WorkManager) {

    companion object {
        private val LOG_TAG = RecurringLocationCollector::class.java.simpleName
    }

    @KoinApiExtension
    fun enqueueWork() {
        Log.d(LOG_TAG, "enqueueWork()")

        workManager.enqueueUniquePeriodicWork(
            SAVE_LOCATION_WORK_TAG,
            ExistingPeriodicWorkPolicy.REPLACE,
            PeriodicWorkRequestBuilder<SaveLocationWorker>(
                SAVE_LOCATION_WORK_INTERVAL_TIME_VALUE,
                SAVE_LOCATION_WORK_INTERVAL_TIME_UNIT
            ).build()
        )
    }

    @KoinApiExtension
    class SaveLocationWorker(appContext: Context, workerParams: WorkerParameters) :
        CoroutineWorker(appContext, workerParams), KoinComponent {
        private val locationFetcher: LocationFetcher by inject()
        private val locationRepository: LocationRepository by inject()

        override suspend fun doWork() = try {
            Log.d(LOG_TAG, "Starting saving location work")

            val location = withTimeout(FETCH_LOCATION_TIMEOUT_MILLISECONDS) {
                locationFetcher.fetchLocation()
            }

            withContext(Dispatchers.IO) {
                locationRepository.insertLocationData(
                    LocationData(
                        location.latitude,
                        location.longitude,
                        System.currentTimeMillis()
                    )
                )
            }

            Result.success()
        } catch (e: Exception) {
            Log.w(LOG_TAG, "Exception while performing saving location work", e)

            e.printStackTrace()

            Result.failure()
        }
    }
}
