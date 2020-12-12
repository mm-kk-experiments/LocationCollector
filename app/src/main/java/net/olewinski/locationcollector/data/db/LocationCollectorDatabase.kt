package net.olewinski.locationcollector.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import net.olewinski.locationcollector.data.db.DbSchemaConstants.LOCATION_COLLECTOR_DATABASE_VERSION
import net.olewinski.locationcollector.data.db.dao.LocationDataDao
import net.olewinski.locationcollector.data.db.entities.LocationData

@Database(entities = [LocationData::class], version = LOCATION_COLLECTOR_DATABASE_VERSION)
abstract class LocationCollectorDatabase : RoomDatabase() {
    abstract fun getLocationDataDao(): LocationDataDao
}
