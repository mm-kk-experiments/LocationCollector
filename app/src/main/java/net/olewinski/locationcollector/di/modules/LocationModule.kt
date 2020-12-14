package net.olewinski.locationcollector.di.modules

import android.content.Context
import com.google.android.gms.location.LocationServices
import net.olewinski.locationcollector.location.LocationCollectionScheduler
import net.olewinski.locationcollector.location.LocationFetcher
import org.koin.dsl.module

val locationModule = module {
    factory { LocationServices.getFusedLocationProviderClient(get(Context::class)) }
    factory { LocationFetcher(get(), get()) }
    factory { LocationCollectionScheduler(get(), get()) }
}
