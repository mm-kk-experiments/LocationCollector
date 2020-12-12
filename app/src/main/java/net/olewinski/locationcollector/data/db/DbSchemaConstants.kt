package net.olewinski.locationcollector.data.db

object DbSchemaConstants {
    const val LOCATION_COLLECTOR_DATABASE_VERSION = 1

    const val LOCATION_COLLECTOR_DATABASE_NAME = "location_collector_database"

    const val LOCATION_DATA_ENTITY_TABLE_NAME = "location_data"
    const val LOCATION_DATA_ENTITY_LATITUDE_COLUMN_NAME = "latitude"
    const val LOCATION_DATA_ENTITY_LONGITUDE_COLUMN_NAME = "longitude"
    const val LOCATION_DATA_ENTITY_TIMESTAMP_COLUMN_NAME = "timestamp"
    const val LOCATION_DATA_ENTITY_ID_COLUMN_NAME = "id"
}
