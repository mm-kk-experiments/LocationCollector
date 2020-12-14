package net.olewinski.locationcollector

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import net.olewinski.locationcollector.location.LocationCollectionScheduler
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BootCompletedReceiver : BroadcastReceiver(), KoinComponent {

    companion object {
        private val LOG_TAG = BootCompletedReceiver::class.java.simpleName
    }

    private val locationCollectionScheduler: LocationCollectionScheduler by inject()

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(LOG_TAG, "onReceive: ${intent.action}")

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            locationCollectionScheduler.scheduleImmediate()
        }
    }
}
