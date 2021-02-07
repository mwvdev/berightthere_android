package mwvdev.berightthere.android.persistence

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runBlockingTest
import mwvdev.berightthere.android.TestDataHelper
import mwvdev.berightthere.android.getOrAwaitValue
import mwvdev.berightthere.android.persistence.dao.LocationDao
import mwvdev.berightthere.android.persistence.dao.TripDao
import mwvdev.berightthere.android.persistence.entity.Location
import mwvdev.berightthere.android.persistence.entity.Trip
import mwvdev.berightthere.android.persistence.entity.TripWithLocations
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {
    private lateinit var tripDao: TripDao
    private lateinit var locationDao: LocationDao

    private lateinit var appDatabase: AppDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

        tripDao = appDatabase.tripDao()
        locationDao = appDatabase.locationDao()
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun canWriteTripAndGetTripById() = runBlockingTest {
        val trip = TestDataHelper.trip()
        tripDao.insert(trip)

        val actualTrip = tripDao.get(trip.tripIdentifier).getOrAwaitValue()

        assertThat(actualTrip).isNotNull()
        assertThat(actualTrip.tripIdentifier).isEqualTo(trip.tripIdentifier)
    }

    @Test
    fun canWriteLocationsAndGetTripWithLocationsById() = runBlockingTest {
        val trip = TestDataHelper.trip()
        tripDao.insert(trip)

        val location = TestDataHelper.location()
        locationDao.insertAll(location)

        val actualTripWithLocations = tripDao
            .getTripWithLocations(trip.tripIdentifier)
            .getOrAwaitValue()

        assertTripWithLocations(actualTripWithLocations, trip, location)
    }

    @Test
    fun canWriteLocationsAndGetAllTripsWithLocations() = runBlockingTest {
        val trip1 = Trip("trip1", TestDataHelper.createdAt, TestDataHelper.transportMode)
        val trip2 = Trip("trip2", TestDataHelper.createdAt, TestDataHelper.transportMode)
        tripDao.insertAll(trip1, trip2)

        val location1 = Location(0, "trip1", 0.0, 0.0, 0.0f)
        val location2 = Location(0, "trip2", 0.0, 0.0, 0.0f)
        locationDao.insertAll(location1, location2)

        val actualTripsWithLocations = tripDao
            .getTripsWithLocations()
            .getOrAwaitValue()

        assertThat(actualTripsWithLocations).hasSize(2)
        assertTripWithLocations(actualTripsWithLocations.first(), trip1, location1)
        assertTripWithLocations(actualTripsWithLocations.last(), trip2, location2)
    }
    
    @Test
    fun canDeleteAll() = runBlockingTest {
        val trip = TestDataHelper.trip()
        tripDao.insert(trip)

        tripDao.deleteAll()

        val actualTrip = tripDao.get(trip.tripIdentifier).getOrAwaitValue()
        assertThat(actualTrip).isNull()
    }

    private fun assertTripWithLocations(
        actualTripWithLocations: TripWithLocations,
        expectedTrip: Trip,
        expectedLocation: Location
    ) {
        assertThat(actualTripWithLocations.trip).isEqualTo(expectedTrip)
        assertThat(actualTripWithLocations.locations).hasSize(1)

        val actualLocation = actualTripWithLocations.locations.first()
        assertThat(actualLocation.latitude).isEqualTo(expectedLocation.latitude)
        assertThat(actualLocation.longitude).isEqualTo(expectedLocation.longitude)
    }

}