package net.olewinski.locationcollector.di.modules

import android.content.Context
import androidx.work.WorkManager
import com.google.android.gms.location.LocationServices
import net.olewinski.locationcollector.location.LocationFetcher
import net.olewinski.locationcollector.location.RecurringLocationCollector
import org.koin.dsl.module

val locationModule = module {
    factory { LocationServices.getFusedLocationProviderClient(get(Context::class)) }
    factory { LocationFetcher(get(), get()) }
    factory { WorkManager.getInstance(get()) }
    factory { RecurringLocationCollector(get()) }
}
