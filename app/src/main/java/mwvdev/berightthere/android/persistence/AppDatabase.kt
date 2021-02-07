package mwvdev.berightthere.android.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mwvdev.berightthere.android.persistence.converters.OffsetDateTimeConverter
import mwvdev.berightthere.android.persistence.converters.TransportModeConverter
import mwvdev.berightthere.android.persistence.dao.LocationDao
import mwvdev.berightthere.android.persistence.dao.TripDao
import mwvdev.berightthere.android.persistence.entity.Location
import mwvdev.berightthere.android.persistence.entity.Trip

@Database(entities = [Trip::class, Location::class], version = 1)
@TypeConverters(OffsetDateTimeConverter::class, TransportModeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tripDao(): TripDao

    abstract fun locationDao(): LocationDao

}
