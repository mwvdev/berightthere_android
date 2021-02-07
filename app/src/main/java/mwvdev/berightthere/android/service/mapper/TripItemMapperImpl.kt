package mwvdev.berightthere.android.service.mapper

import mwvdev.berightthere.android.R
import mwvdev.berightthere.android.model.TransportMode
import mwvdev.berightthere.android.persistence.entity.Location
import mwvdev.berightthere.android.service.DistanceService
import mwvdev.berightthere.android.service.OffsetDateTimeService
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripItemMapperImpl @Inject constructor(
    private val distanceService: DistanceService,
    private val offsetDateTimeService: OffsetDateTimeService
) :
    TripItemMapper {

    override fun mapTitle(createdAt: OffsetDateTime): String {
        return when {
            createdAt.hour in 4..7 -> "Early morning"
            createdAt.hour in 8..10 -> "Morning"
            createdAt.hour == 11 -> "Late morning"
            createdAt.hour == 12 && createdAt.minute == 0 -> "Noon"
            createdAt.hour in 12..13 -> "Early afternoon"
            createdAt.hour in 14..15 -> "Afternoon"
            createdAt.hour == 16 -> "Late afternoon"
            createdAt.hour in 17..18 -> "Early evening"
            createdAt.hour in 19..20 -> "Evening"
            createdAt.hour in 21..23 -> "Night"
            createdAt.hour == 0 && createdAt.minute == 0 -> "Midnight"
            createdAt.hour in 0..3 -> "Late night"
            else -> "Unknown"
        }
    }

    override fun mapTransportModeIcon(transportMode: TransportMode): Int {
        return when (transportMode) {
            TransportMode.Walking -> R.drawable.ic_directions_walk_24dp
            TransportMode.Bicycle -> R.drawable.ic_directions_bike_24dp
            TransportMode.Car -> R.drawable.ic_directions_car_24dp
        }
    }

    override fun mapDistance(locations: List<Location>): String {
        val distanceInMeters = distanceService.totalDistance(locations)

        return if (distanceInMeters < 10_000f) {
            "%.0f m".format(distanceInMeters)
        } else {
            "%.2f km".format(distanceInMeters / 1_000f)
        }
    }

    override fun mapCreatedAt(createdAt: OffsetDateTime): String {
        val nowLocalDate = offsetDateTimeService.now().toLocalDate()

        val dateComponent = when (createdAt.toLocalDate()) {
            nowLocalDate -> "Today"
            nowLocalDate.minusDays(1) -> "Yesterday"
            else -> createdAt.toLocalDate().format(DateTimeFormatter.ISO_DATE)
        }

        val timeComponent = createdAt.format(DateTimeFormatter.ofPattern("H:mm"))

        return "$dateComponent, $timeComponent"
    }

}