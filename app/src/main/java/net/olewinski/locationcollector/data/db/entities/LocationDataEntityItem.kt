package net.olewinski.locationcollector.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import net.olewinski.locationcollector.data.db.DbSchemaConstants.LOCATION_DATA_ENTITY_ID_COLUMN_NAME
import net.olewinski.locationcollector.data.db.DbSchemaConstants.LOCATION_DATA_ENTITY_LATITUDE_COLUMN_NAME
import net.olewinski.locationcollector.data.db.DbSchemaConstants.LOCATION_DATA_ENTITY_LONGITUDE_COLUMN_NAME
import net.olewinski.locationcollector.data.db.DbSchemaConstants.LOCATION_DATA_ENTITY_TABLE_NAME
import net.olewinski.locationcollector.data.db.DbSchemaConstants.LOCATION_DATA_ENTITY_TIMESTAMP_COLUMN_NAME

@Entity(tableName = LOCATION_DATA_ENTITY_TABLE_NAME)
data class LocationDataEntityItem(
    @ColumnInfo(name = LOCATION_DATA_ENTITY_LATITUDE_COLUMN_NAME)
    val latitude: Double,

    @ColumnInfo(name = LOCATION_DATA_ENTITY_LONGITUDE_COLUMN_NAME)
    val longitude: Double,

    @ColumnInfo(name = LOCATION_DATA_ENTITY_TIMESTAMP_COLUMN_NAME)
    val timestamp: Long,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = LOCATION_DATA_ENTITY_ID_COLUMN_NAME)
    val id: Long = 0L
)
