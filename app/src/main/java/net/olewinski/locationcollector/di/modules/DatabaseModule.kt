package net.olewinski.locationcollector.di.modules

import android.content.Context
import androidx.room.Room
import net.olewinski.locationcollector.data.db.DbSchemaConstants.LOCATION_COLLECTOR_DATABASE_NAME
import net.olewinski.locationcollector.data.db.LocationCollectorDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { getLocationCollectorDatabase(get()) }
}

fun getLocationCollectorDatabase(context: Context): LocationCollectorDatabase =
    Room.databaseBuilder(
        context,
        LocationCollectorDatabase::class.java,
        LOCATION_COLLECTOR_DATABASE_NAME
    ).build()
