package mwvdev.berightthere.android.service.mapper

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import mwvdev.berightthere.android.R
import mwvdev.berightthere.android.model.TransportMode
import mwvdev.berightthere.android.persistence.entity.Location
import mwvdev.berightthere.android.service.DistanceService
import mwvdev.berightthere.android.service.OffsetDateTimeService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.time.*
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class TripItemMapperImplTests {

    @Mock
    private lateinit var distanceService: DistanceService

    @Mock
    private lateinit var offsetDateTimeService: OffsetDateTimeService

    private lateinit var mapper: TripItemMapperImpl

    @Before
    fun setUp() {
        Locale.setDefault(Locale.US)

        mapper = TripItemMapperImpl(distanceService, offsetDateTimeService)
    }

    @Test
    fun canMapTitle() {
        val inputExpectedPairs = arrayOf(
            Pair(createOffsetDateTime(4, 0), "Early morning"),
            Pair(createOffsetDateTime(7, 59), "Early morning"),
            Pair(createOffsetDateTime(8, 0), "Morning"),
            Pair(createOffsetDateTime(10, 59), "Morning"),
            Pair(createOffsetDateTime(11, 0), "Late morning"),
            Pair(createOffsetDateTime(11, 59), "Late morning"),
            Pair(createOffsetDateTime(12, 0), "Noon"),
            Pair(createOffsetDateTime(12, 1), "Early afternoon"),
            Pair(createOffsetDateTime(13, 59), "Early afternoon"),
            Pair(createOffsetDateTime(14, 0), "Afternoon"),
            Pair(createOffsetDateTime(15, 59), "Afternoon"),
            Pair(createOffsetDateTime(16, 0), "Late afternoon"),
            Pair(createOffsetDateTime(16, 59), "Late afternoon"),
            Pair(createOffsetDateTime(17, 0), "Early evening"),
            Pair(createOffsetDateTime(18, 59), "Early evening"),
            Pair(createOffsetDateTime(19, 0), "Evening"),
            Pair(createOffsetDateTime(20, 59), "Evening"),
            Pair(createOffsetDateTime(21, 0), "Night"),
            Pair(createOffsetDateTime(23, 59), "Night"),
            Pair(createOffsetDateTime(0, 0), "Midnight"),
            Pair(createOffsetDateTime(0, 1), "Late night"),
            Pair(createOffsetDateTime(3, 59), "Late night")
        )

        inputExpectedPairs.forEach {
            val createdAt = it.first
            val expected = it.second

            val actual = mapper.mapTitle(createdAt)

            assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun canMapTransportMode() {
        val inputExpectedPairs = arrayOf(
            Pair(TransportMode.Walking, R.drawable.ic_directions_walk_24dp),
            Pair(TransportMode.Bicycle, R.drawable.ic_directions_bike_24dp),
            Pair(TransportMode.Car, R.drawable.ic_directions_car_24dp)
        )

        inputExpectedPairs.forEach {
            val transportMode = it.first
            val expected = it.second

            val actual = mapper.mapTransportModeIcon(transportMode)

            assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun canMapShortDistance() {
        val locations: List<Location> = emptyList()
        whenever(distanceService.totalDistance(locations)).thenReturn(240.5f)

        val actual = mapper.mapDistance(locations)

        assertThat(actual).isEqualTo("241 m")
    }

    @Test
    fun canMapLongDistance() {
        val locations: List<Location> = emptyList()
        whenever(distanceService.totalDistance(locations)).thenReturn(10550.6f)

        val actual = mapper.mapDistance(locations)

        assertThat(actual).isEqualTo("10.55 km")
    }

    @Test
    fun canMapCreatedAt() {
        val now = OffsetDateTime.of(2020, 10, 11, 15, 10, 20, 0, ZoneOffset.UTC)
        whenever(offsetDateTimeService.now()).thenReturn(now)

        val inputExpectedPairs = arrayOf(
            Pair(now, "Today, 15:10"),
            Pair(now.minusHours(1).minusMinutes(5), "Today, 14:05"),
            Pair(now.minusHours(16), "Yesterday, 23:10"),
            Pair(now.minusDays(1), "Yesterday, 15:10"),
            Pair(now.minusDays(2), "2020-10-09, 15:10")
        )

        inputExpectedPairs.forEach {
            val createdAt = it.first
            val expected = it.second

            val actual = mapper.mapCreatedAt(createdAt)

            assertThat(actual).isEqualTo(expected)
        }
    }

    private fun createOffsetDateTime(
        hour: Int, minute: Int
    ): OffsetDateTime {
        val localDate = LocalDate.of(2020, 10, 7)
        val localTime = LocalTime.of(hour, minute)

        return OffsetDateTime.of(localDate, localTime, ZoneOffset.UTC)
    }

}