package net.olewinski.locationcollector.viewmodels

import androidx.lifecycle.ViewModel
import net.olewinski.locationcollector.data.repository.LocationRepository
import net.olewinski.locationcollector.location.RecurringLocationCollector
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class MainFragmentViewModel(
    recurringLocationCollector: RecurringLocationCollector,
    locationRepository: LocationRepository
) : ViewModel() {
    init {
        recurringLocationCollector.enqueueWork()
    }

    val allLocationData = locationRepository.allLocationData
}
