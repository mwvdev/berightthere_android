package mwvdev.berightthere.android

import mwvdev.berightthere.android.dto.CheckinDto
import mwvdev.berightthere.android.dto.LocationDto
import mwvdev.berightthere.android.model.MeasuredLocation
import mwvdev.berightthere.android.model.TransportMode
import mwvdev.berightthere.android.persistence.entity.Location
import mwvdev.berightthere.android.persistence.entity.Trip
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

class TestDataHelper {
    companion object {
        const val tripIdentifier = "0c98b95e-848f-4589-a7f9-dcc7dde95725"
        val createdAt: OffsetDateTime = OffsetDateTime.of(LocalDateTime.of(2020, 7, 3, 23, 8), ZoneOffset.UTC)
        val transportMode = TransportMode.Car

        const val latitude = 55.6739062
        const val longitude = 12.5556993
        const val accuracy = 15.5f
        val measuredAt: OffsetDateTime = OffsetDateTime.of(LocalDateTime.of(2020, 7, 3, 23, 9), ZoneOffset.UTC)

        fun checkinDto() = CheckinDto(tripIdentifier)

        fun trip() = Trip(tripIdentifier, createdAt, transportMode)

        fun locationDto() = LocationDto(
            latitude,
            longitude,
            accuracy,
            measuredAt
        )

        fun location() = Location(
            0,
            tripIdentifier,
            latitude,
            longitude,
            accuracy
        )

        fun measuredLocation() = MeasuredLocation(
            latitude,
            longitude,
            accuracy
        )
    }
}