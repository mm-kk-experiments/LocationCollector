package net.olewinski.locationcollector.di.modules

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import org.koin.dsl.module

val systemServicesModule = module {
    factory { get(Context::class.java).getSystemService(Context.ALARM_SERVICE) as AlarmManager }
    factory { get(Context::class.java).getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
}
