package net.olewinski.locationcollector.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import net.olewinski.locationcollector.data.db.DbSchemaConstants.LOCATION_COLLECTOR_DATABASE_VERSION
import net.olewinski.locationcollector.data.db.dao.LocationDataEntityDao
import net.olewinski.locationcollector.data.db.entities.LocationDataEntityItem

@Database(entities = [LocationDataEntityItem::class], version = LOCATION_COLLECTOR_DATABASE_VERSION)
abstract class LocationCollectorDatabase : RoomDatabase() {
    abstract fun getLocationDataEntityDao(): LocationDataEntityDao
}
