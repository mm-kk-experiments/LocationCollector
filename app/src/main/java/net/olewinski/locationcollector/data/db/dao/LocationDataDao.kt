package net.olewinski.locationcollector.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import net.olewinski.locationcollector.data.db.DbSchemaConstants.LOCATION_DATA_ENTITY_TABLE_NAME
import net.olewinski.locationcollector.data.db.entities.LocationData

@Dao
interface LocationDataDao {
    @Insert
    suspend fun insert(locationData: LocationData): Long

    @Query("SELECT * FROM $LOCATION_DATA_ENTITY_TABLE_NAME")
    fun getAllLocationData(): LiveData<List<LocationData>>
}
