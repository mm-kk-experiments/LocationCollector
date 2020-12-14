package net.olewinski.locationcollector.location

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import net.olewinski.locationcollector.R
import net.olewinski.locationcollector.data.models.LocationData
import net.olewinski.locationcollector.data.repository.LocationRepository
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

private const val BACKGROUND_LOCATION_CHECK_NOTIFICATION_CHANNEL_ID =
    "BACKGROUND_LOCATION_CHECK_NOTIFICATION_CHANNEL_ID"
private const val FETCH_LOCATION_TIMEOUT_MILLISECONDS = 60000L
private const val SAVE_LOCATION_WORK_TAG = "SAVE_LOCATION_WORK_TAG"
private const val SAVE_LOCATION_WORK_INTERVAL_TIME_VALUE = 60L
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
    class SaveLocationWorker(private val appContext: Context, workerParams: WorkerParameters) :
        CoroutineWorker(appContext, workerParams), KoinComponent {
        private val locationFetcher: LocationFetcher by inject()
        private val locationRepository: LocationRepository by inject()

        private val notificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        override suspend fun doWork() = try {
            Log.d(LOG_TAG, "Starting saving location work")

            makeWorkForeground()

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

        private suspend fun makeWorkForeground() {
            createNotificationChannel()

            setForeground(
                ForegroundInfo(
                    R.id.background_location_check_notification_id,
                    NotificationCompat.Builder(
                        appContext,
                        BACKGROUND_LOCATION_CHECK_NOTIFICATION_CHANNEL_ID
                    ).setSmallIcon(R.drawable.background_location_check_notification_icon)
                        .setContentTitle(appContext.getString(R.string.app_name))
                        .setContentText(appContext.getString(R.string.background_location_check_notification_content_text))
                        .setPriority(NotificationCompat.PRIORITY_LOW).build(),
                    FOREGROUND_SERVICE_TYPE_LOCATION
                )
            )
        }

        private fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    BACKGROUND_LOCATION_CHECK_NOTIFICATION_CHANNEL_ID,
                    appContext.getString(R.string.background_location_check_notification_channel_name),
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description =
                        appContext.getString(R.string.background_location_check_notification_channel_description)
                }

                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}
