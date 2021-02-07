package mwvdev.berightthere.android.service

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import mwvdev.berightthere.android.persistence.entity.Location
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DistanceServiceImplTest {

    private val distanceService = DistanceServiceImpl()

    @Test
    fun canCalculateDistanceBetween() {
        val locations = listOf(
            createLocation(1, 55.6739062, 12.5556993),
            createLocation(2, 55.6746322, 12.5585318),
            createLocation(3, 55.6764229, 12.5588751)
        )

        val actual = distanceService.totalDistance(locations)

        Truth.assertThat(actual).isEqualTo(396.22083f)
    }

    private fun createLocation(locationEntityId: Long, latitude: Double, longitude: Double): Location {
        return Location(
            locationEntityId,
            "linkedTripIdentifier",
            latitude,
            longitude,
            0f
        )
    }

}