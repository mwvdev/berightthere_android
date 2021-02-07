package mwvdev.berightthere.android.persistence.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TripWithLocations(
    @Embedded val trip: Trip,
    @Relation(
        parentColumn = "tripIdentifier",
        entityColumn = "linkedTripIdentifier"
    )
    val locations: List<Location>
)