package net.olewinski.locationcollector.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import net.olewinski.locationcollector.data.db.DbSchemaConstants.LOCATION_DATA_ENTITY_TABLE_NAME
import net.olewinski.locationcollector.data.db.DbSchemaConstants.LOCATION_DATA_ENTITY_TIMESTAMP_COLUMN_NAME
import net.olewinski.locationcollector.data.db.entities.LocationDataEntityItem

@Dao
interface LocationDataEntityDao {
    @Insert
    suspend fun insert(locationDataEntityItem: LocationDataEntityItem): Long

    @Query("SELECT * FROM $LOCATION_DATA_ENTITY_TABLE_NAME ORDER BY $LOCATION_DATA_ENTITY_TIMESTAMP_COLUMN_NAME DESC")
    fun getAllLocationDataEntityItems(): LiveData<List<LocationDataEntityItem>>
}
