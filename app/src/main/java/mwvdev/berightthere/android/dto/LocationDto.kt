package mwvdev.berightthere.android.dto

import java.time.OffsetDateTime

data class LocationDto(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val measuredAt: OffsetDateTime
)