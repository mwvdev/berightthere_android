package mwvdev.berightthere.android.persistence.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Trip::class,
        parentColumns = ["tripIdentifier"],
        childColumns = ["linkedTripIdentifier"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("linkedTripIdentifier")]
)
data class Location(
    @PrimaryKey(autoGenerate = true) val locationEntityId: Long,
    val linkedTripIdentifier: String,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float
)