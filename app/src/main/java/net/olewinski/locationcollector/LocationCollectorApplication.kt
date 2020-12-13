@file:Suppress("unused")

package net.olewinski.locationcollector

import android.app.Application
import net.olewinski.locationcollector.di.modules.databaseModule
import net.olewinski.locationcollector.di.modules.repositoryModule
import net.olewinski.locationcollector.di.modules.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class LocationCollectorApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@LocationCollectorApplication)

            modules(listOf(databaseModule, repositoryModule, viewModelsModule))
        }
    }
}
