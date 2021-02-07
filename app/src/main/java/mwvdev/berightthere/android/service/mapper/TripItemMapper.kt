package mwvdev.berightthere.android.service.mapper

import mwvdev.berightthere.android.model.TransportMode
import mwvdev.berightthere.android.persistence.entity.Location
import java.time.OffsetDateTime

interface TripItemMapper {

    fun mapTitle(createdAt: OffsetDateTime): String

    fun mapTransportModeIcon(transportMode: TransportMode): Int

    fun mapDistance(locations: List<Location>): String

    fun mapCreatedAt(createdAt: OffsetDateTime): String

}