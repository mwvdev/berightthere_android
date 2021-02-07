package mwvdev.berightthere.android.persistence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import mwvdev.berightthere.android.model.TransportMode
import java.time.OffsetDateTime

@Entity
data class Trip(
    @PrimaryKey val tripIdentifier: String,
    val createdAt: OffsetDateTime,
    val transportMode: TransportMode
)