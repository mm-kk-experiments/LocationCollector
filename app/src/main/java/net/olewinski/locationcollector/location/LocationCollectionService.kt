package net.olewinski.locationcollector.location

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import net.olewinski.locationcollector.R
import net.olewinski.locationcollector.data.models.LocationData
import net.olewinski.locationcollector.data.repository.LocationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private const val BACKGROUND_LOCATION_CHECK_NOTIFICATION_CHANNEL_ID =
    "BACKGROUND_LOCATION_CHECK_NOTIFICATION_CHANNEL_ID"
private const val FETCH_LOCATION_TIMEOUT_MILLISECONDS = 60000L

class LocationCollectionService : Service(), KoinComponent {

    companion object {
        private val LOG_TAG = LocationCollectionService::class.java.simpleName
    }

    private val locationFetcher: LocationFetcher by inject()
    private val locationRepository: LocationRepository by inject()
    private val notificationManager: NotificationManager by inject()
    private val locationCollectionScheduler: LocationCollectionScheduler by inject()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(LOG_TAG, "Starting service")

        makeForeground()

        GlobalScope.launch {
            try {
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
            } catch (e: Exception) {
                Log.w(LOG_TAG, "Exception while performing saving location work", e)

                e.printStackTrace()
            } finally {
                Log.d(LOG_TAG, "Stopping service")

                // Scheduling next attempt
                locationCollectionScheduler.scheduleDelayed()

                stopForeground(true)
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun makeForeground() {
        createNotificationChannel()

        val notification =
            NotificationCompat.Builder(this, BACKGROUND_LOCATION_CHECK_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.background_location_check_notification_icon)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.background_location_check_notification_content_text))
                .setPriority(NotificationCompat.PRIORITY_LOW).build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                R.id.background_location_check_notification_id,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            )
        } else {
            startForeground(R.id.background_location_check_notification_id, notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                BACKGROUND_LOCATION_CHECK_NOTIFICATION_CHANNEL_ID,
                getString(R.string.background_location_check_notification_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description =
                    getString(R.string.background_location_check_notification_channel_description)
            }

            notificationManager.createNotificationChannel(channel)
        }
    }
}
