package mwvdev.berightthere.android.repository

import androidx.lifecycle.LiveData
import mwvdev.berightthere.android.BuildConfig
import mwvdev.berightthere.android.dto.LocationDto
import mwvdev.berightthere.android.exception.AddLocationException
import mwvdev.berightthere.android.exception.CheckinException
import mwvdev.berightthere.android.model.MeasuredLocation
import mwvdev.berightthere.android.model.TransportMode
import mwvdev.berightthere.android.persistence.dao.LocationDao
import mwvdev.berightthere.android.persistence.dao.TripDao
import mwvdev.berightthere.android.persistence.entity.Location
import mwvdev.berightthere.android.persistence.entity.Trip
import mwvdev.berightthere.android.persistence.entity.TripWithLocations
import mwvdev.berightthere.android.service.BeRightThereService
import mwvdev.berightthere.android.service.OffsetDateTimeService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripRepository @Inject constructor(
    private val beRightThereService: BeRightThereService,
    private val tripDao: TripDao,
    private val locationDao: LocationDao,
    private val offsetDateTimeService: OffsetDateTimeService
) : ITripRepository {

    override fun getTripUrl(tripIdentifier: String): String {
        return "${BuildConfig.BERIGHTTHERE_API_ENDPOINT}/trip/${tripIdentifier}"
    }

    override fun getTrip(tripIdentifier: String): LiveData<Trip> {
        return tripDao.get(tripIdentifier)
    }

    override fun getTripWithLocations(tripIdentifier: String): LiveData<TripWithLocations> {
        return tripDao.getTripWithLocations(tripIdentifier)
    }

    override fun getTripsWithLocations(): LiveData<List<TripWithLocations>> {
        return tripDao.getTripsWithLocations()
    }

    override suspend fun checkin(transportMode: TransportMode): String {
        try {
            val checkinDto = beRightThereService.checkin()

            tripDao.insert(Trip(checkinDto.identifier, offsetDateTimeService.now(), transportMode))

            return checkinDto.identifier
        } catch (cause: Throwable) {
            throw CheckinException("Checkin failed: ${cause.message}", cause)
        }
    }

    override suspend fun addLocation(tripIdentifier: String, measuredLocation: MeasuredLocation) {
        try {
            val locationDto = LocationDto(
                measuredLocation.latitude,
                measuredLocation.longitude,
                measuredLocation.accuracy,
                offsetDateTimeService.now()
            )
            beRightThereService.addLocation(tripIdentifier, locationDto)

            val location = Location(
                0,
                tripIdentifier,
                measuredLocation.latitude,
                measuredLocation.longitude,
                measuredLocation.accuracy
            )
            locationDao.insertAll(location)
        } catch (cause: Throwable) {
            throw AddLocationException("Adding location failed: ${cause.message}", cause)
        }
    }

    override suspend fun delete(trip: Trip) {
        tripDao.delete(trip)
    }

    override suspend fun deleteAll() {
        tripDao.deleteAll()
    }

}