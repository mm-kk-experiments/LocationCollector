package net.olewinski.locationcollector.location

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.util.Log
import net.olewinski.locationcollector.R

private const val REQUEST_CODE = R.id.start_location_collection_service_request_code

class LocationCollectionScheduler(context: Context, private val alarmManager: AlarmManager) {
    private val applicationContext = context.applicationContext

    companion object {
        private val LOG_TAG = LocationCollectionScheduler::class.java.simpleName
    }

    fun scheduleImmediate() {
        Log.d(LOG_TAG, "scheduleImmediate()")

        val locationCollectionServiceIntent =
            Intent(applicationContext, LocationCollectionService::class.java)
        val locationCollectionServicePendingIntent =
            getLocationCollectionServicePendingIntent(locationCollectionServiceIntent)

        // If any location check is scheduled, we want to cancel it because we want the
        // scheduleImmediate() method to call the service immediately.
        alarmManager.cancel(locationCollectionServicePendingIntent)

        locationCollectionServicePendingIntent.cancel()

        // Starting the service immediately
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            applicationContext.startForegroundService(locationCollectionServiceIntent)
        } else {
            applicationContext.startService(locationCollectionServiceIntent)
        }
    }

    fun scheduleDelayed() {
        Log.d(LOG_TAG, "scheduleDelayed()")

        val locationCollectionServiceIntent =
            Intent(applicationContext, LocationCollectionService::class.java)
        val locationCollectionServicePendingIntent =
            getLocationCollectionServicePendingIntent(locationCollectionServiceIntent)

        // If any location check is scheduled, we want to cancel it because we want the
        // scheduleDelayed() method to schedule it at exact time from NOW.
        alarmManager.cancel(locationCollectionServicePendingIntent)

        // Scheduling delayed service start
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HOUR,
                locationCollectionServicePendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HOUR,
                locationCollectionServicePendingIntent
            )
        }
    }

    private fun getLocationCollectionServicePendingIntent(locationCollectionServiceIntent: Intent) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getForegroundService(
                applicationContext,
                REQUEST_CODE,
                locationCollectionServiceIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        } else {
            PendingIntent.getService(
                applicationContext,
                REQUEST_CODE,
                locationCollectionServiceIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }
}
