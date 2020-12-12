package net.olewinski.locationcollector.data.repository

import androidx.lifecycle.map
import net.olewinski.locationcollector.data.db.LocationCollectorDatabase
import net.olewinski.locationcollector.data.db.entities.LocationDataEntityItem
import net.olewinski.locationcollector.data.models.LocationData

class LocationRepository(locationCollectorDatabase: LocationCollectorDatabase) {
    private val locationDataDao = locationCollectorDatabase.getLocationDataEntityDao()

    val allLocationData =
        locationDataDao.getAllLocationDataEntityItems().map { allLocationDataEntityItems ->
            allLocationDataEntityItems.map { locationDataEntityItem ->
                LocationData(
                    locationDataEntityItem.latitude,
                    locationDataEntityItem.longitude,
                    locationDataEntityItem.timestamp,
                    locationDataEntityItem.id
                )
            }
        }

    suspend fun insertLocationData(locationData: LocationData) = locationDataDao.insert(
        LocationDataEntityItem(
            locationData.latitude,
            locationData.longitude,
            locationData.timestamp
        )
    )
}
