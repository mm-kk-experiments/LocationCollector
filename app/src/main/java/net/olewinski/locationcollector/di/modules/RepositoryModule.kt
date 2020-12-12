package net.olewinski.locationcollector.di.modules

import net.olewinski.locationcollector.data.repository.LocationRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { LocationRepository(get()) }
}
