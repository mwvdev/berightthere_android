package mwvdev.berightthere.android.repository

import androidx.lifecycle.LiveData
import mwvdev.berightthere.android.model.MeasuredLocation
import mwvdev.berightthere.android.model.TransportMode
import mwvdev.berightthere.android.persistence.entity.Trip
import mwvdev.berightthere.android.persistence.entity.TripWithLocations

interface ITripRepository {

    fun getTripUrl(tripIdentifier: String): String

    fun getTrip(tripIdentifier: String): LiveData<mwvdev.berightthere.android.persistence.entity.Trip>

    fun getTripWithLocations(tripIdentifier: String): LiveData<TripWithLocations>

    fun getTripsWithLocations(): LiveData<List<TripWithLocations>>

    suspend fun checkin(transportMode: TransportMode): String

    suspend fun addLocation(tripIdentifier: String, measuredLocation: MeasuredLocation)

    suspend fun delete(trip: Trip)

    suspend fun deleteAll()

}