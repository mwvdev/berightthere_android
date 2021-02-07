package mwvdev.berightthere.android.service

import android.location.Location.distanceBetween
import mwvdev.berightthere.android.persistence.entity.Location
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DistanceServiceImpl @Inject constructor() : DistanceService {

    override fun totalDistance(locations: List<Location>): Float {
        return locations
            .zipWithNext()
            .map { distanceBetween(it) }
            .sum()
    }

    private fun distanceBetween(locationPair: Pair<Location, Location>): Float {
        val results = FloatArray(3)

        distanceBetween(
            locationPair.first.latitude,
            locationPair.first.longitude,
            locationPair.second.latitude,
            locationPair.second.longitude,
            results
        )

        return results[0]
    }

}