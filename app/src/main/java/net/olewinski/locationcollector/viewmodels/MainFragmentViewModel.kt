package net.olewinski.locationcollector.viewmodels

import androidx.lifecycle.ViewModel
import net.olewinski.locationcollector.data.repository.LocationRepository
import net.olewinski.locationcollector.location.LocationCollectionScheduler
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class MainFragmentViewModel(
    locationCollectionScheduler: LocationCollectionScheduler,
    locationRepository: LocationRepository
) : ViewModel() {
    init {
        locationCollectionScheduler.scheduleImmediate()
    }

    val allLocationData = locationRepository.allLocationData
}
