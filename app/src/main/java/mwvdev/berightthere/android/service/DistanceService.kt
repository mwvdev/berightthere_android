package mwvdev.berightthere.android.service

import mwvdev.berightthere.android.persistence.entity.Location

interface DistanceService {

    fun totalDistance(locations: List<Location>): Float

}