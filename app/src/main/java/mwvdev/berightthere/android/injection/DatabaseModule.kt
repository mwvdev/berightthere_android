package mwvdev.berightthere.android.injection

import androidx.room.Room
import dagger.Module
import dagger.Provides
import mwvdev.berightthere.android.BeRightThereApplication
import mwvdev.berightthere.android.persistence.AppDatabase
import mwvdev.berightthere.android.persistence.dao.LocationDao
import mwvdev.berightthere.android.persistence.dao.TripDao
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: BeRightThereApplication): AppDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            "berightthere"
        ).build()
    }

    @Singleton
    @Provides
    fun provideTripDao(appDatabase: AppDatabase): TripDao {
        return appDatabase.tripDao()
    }


    @Singleton
    @Provides
    fun provideLocationDao(appDatabase: AppDatabase): LocationDao {
        return appDatabase.locationDao()
    }

}