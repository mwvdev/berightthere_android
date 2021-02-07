package mwvdev.berightthere.android.fake

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import mwvdev.berightthere.android.TestDataHelper
import mwvdev.berightthere.android.model.MeasuredLocation
import mwvdev.berightthere.android.model.TransportMode
import mwvdev.berightthere.android.persistence.entity.Trip
import mwvdev.berightthere.android.persistence.entity.TripWithLocations
import mwvdev.berightthere.android.repository.ITripRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeTripRepository @Inject constructor() : ITripRepository {

    override fun getTrip(tripIdentifier: String): LiveData<Trip> {
        return MutableLiveData(
            Trip(
                tripIdentifier,
                TestDataHelper.createdAt,
                TestDataHelper.transportMode
            )
        )
    }

    override suspend fun checkin(transportMode: TransportMode): String {
        return TestDataHelper.tripIdentifier
    }

    override fun getTripWithLocations(tripIdentifier: String): LiveData<TripWithLocations> {
        return MutableLiveData(
            TripWithLocations(
                Trip(tripIdentifier, TestDataHelper.createdAt, TestDataHelper.transportMode),
                emptyList()
            )
        )
    }

    override fun getTripsWithLocations(): LiveData<List<TripWithLocations>> {
        return MutableLiveData(
            listOf(
                TripWithLocations(TestDataHelper.trip(), emptyList())
            )
        )
    }

    override suspend fun addLocation(tripIdentifier: String, measuredLocation: MeasuredLocation) {
    }

    override fun getTripUrl(tripIdentifier: String): String {
        return "trip URL"
    }

    override suspend fun delete(trip: Trip) {
    }

    override suspend fun deleteAll() {
    }

}
