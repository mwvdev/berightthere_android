package mwvdev.berightthere.android.repository

import androidx.lifecycle.LiveData
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.test.runBlockingTest
import mwvdev.berightthere.android.BuildConfig
import mwvdev.berightthere.android.TestDataHelper
import mwvdev.berightthere.android.dto.CheckinDto
import mwvdev.berightthere.android.dto.LocationDto
import mwvdev.berightthere.android.exception.AddLocationException
import mwvdev.berightthere.android.exception.CheckinException
import mwvdev.berightthere.android.model.MeasuredLocation
import mwvdev.berightthere.android.persistence.dao.LocationDao
import mwvdev.berightthere.android.persistence.dao.TripDao
import mwvdev.berightthere.android.persistence.entity.Trip
import mwvdev.berightthere.android.persistence.entity.TripWithLocations
import mwvdev.berightthere.android.service.BeRightThereService
import mwvdev.berightthere.android.service.OffsetDateTimeService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TripRepositoryTest {

    @Mock
    private lateinit var beRightThereService: BeRightThereService

    @Mock
    private lateinit var tripDao: TripDao

    @Mock
    private lateinit var locationDao: LocationDao

    @Mock
    private lateinit var offsetDateTimeService: OffsetDateTimeService

    private lateinit var tripRepository: TripRepository

    @Before
    fun setUp() {
        whenever(offsetDateTimeService.now()).thenReturn(TestDataHelper.measuredAt)

        tripRepository =
            TripRepository(beRightThereService, tripDao, locationDao, offsetDateTimeService)
    }

    @Test
    fun canGetTripUrl() {
        val expectedTripUrl =
            "${BuildConfig.BERIGHTTHERE_API_ENDPOINT}/trip/${TestDataHelper.tripIdentifier}"

        val actualTripUrl = tripRepository.getTripUrl(TestDataHelper.tripIdentifier)

        assertThat(actualTripUrl).isEqualTo(expectedTripUrl)
    }

    @Test
    fun canGetTrip() = runBlockingTest {
        val expectedTrip: LiveData<Trip> = mock()
        whenever(tripDao.get(TestDataHelper.tripIdentifier)).thenReturn(expectedTrip)

        val actualTrip = tripRepository.getTrip(TestDataHelper.tripIdentifier)

        assertThat(actualTrip).isEqualTo(expectedTrip)
    }

    @Test
    fun canGetTripWithLocations() = runBlockingTest {
        val expectedTripWithLocations: LiveData<TripWithLocations> = mock()
        whenever(tripDao.getTripWithLocations(TestDataHelper.tripIdentifier)).thenReturn(
            expectedTripWithLocations
        )

        val actualTripWithLocations =
            tripRepository.getTripWithLocations(TestDataHelper.tripIdentifier)

        assertThat(actualTripWithLocations).isEqualTo(expectedTripWithLocations)
    }

    @Test
    fun canGetTripsWithLocations() = runBlockingTest {
        val expectedTripWithLocations: LiveData<List<TripWithLocations>> = mock()
        whenever(tripDao.getTripsWithLocations()).thenReturn(expectedTripWithLocations)

        val actualTripWithLocations = tripRepository.getTripsWithLocations()

        assertThat(actualTripWithLocations).isEqualTo(expectedTripWithLocations)
    }

    @Test
    fun canCheckin() = runBlockingTest {
        val checkinDto = CheckinDto(TestDataHelper.tripIdentifier)
        whenever(beRightThereService.checkin()).thenReturn(checkinDto)
        whenever(offsetDateTimeService.now()).thenReturn(TestDataHelper.createdAt)

        val actual = tripRepository.checkin(TestDataHelper.transportMode)

        assertThat(actual).isEqualTo(checkinDto.identifier)

        argumentCaptor<Trip>().apply {
            verify(tripDao).insert(capture())

            assertThat(firstValue.tripIdentifier).isEqualTo(TestDataHelper.tripIdentifier)
            assertThat(firstValue.createdAt).isEqualTo(TestDataHelper.createdAt)
            assertThat(firstValue.transportMode).isEqualTo(firstValue.transportMode)
        }
    }

    @Test(expected = CheckinException::class)
    fun checkinRethrowsWhenWebServiceFails() = runBlockingTest {
        whenever(beRightThereService.checkin()).doThrow(RuntimeException::class)

        tripRepository.checkin(TestDataHelper.transportMode)
    }

    @Test(expected = CheckinException::class)
    fun checkinRethrowsWhenTripDaoFails() = runBlockingTest {
        val checkinDto = CheckinDto(TestDataHelper.tripIdentifier)
        whenever(beRightThereService.checkin()).thenReturn(checkinDto)

        whenever(tripDao.insert(any())).doThrow(RuntimeException::class)

        tripRepository.checkin(TestDataHelper.transportMode)
    }

    @Test
    fun canAddLocation() = runBlockingTest {
        val measuredLocation = MeasuredLocation(
            TestDataHelper.latitude,
            TestDataHelper.longitude,
            TestDataHelper.accuracy
        )
        tripRepository.addLocation(TestDataHelper.tripIdentifier, measuredLocation)

        argumentCaptor<LocationDto>().apply {
            verify(beRightThereService).addLocation(eq(TestDataHelper.tripIdentifier), capture())

            val expectedLocationDto = TestDataHelper.locationDto()
            assertThat(firstValue).isEqualTo(expectedLocationDto)
        }
    }

    @Test(expected = AddLocationException::class)
    fun addLocationRethrowsWhenWebServiceFails() = runBlockingTest {
        whenever(beRightThereService.addLocation(any(), any())).doThrow(RuntimeException::class)

        val measuredLocation = TestDataHelper.measuredLocation()
        tripRepository.addLocation(TestDataHelper.tripIdentifier, measuredLocation)
    }

    @Test(expected = AddLocationException::class)
    fun addLocationRethrowsWhenLocationDaoFails() = runBlockingTest {
        whenever(locationDao.insertAll(anyVararg())).doThrow(RuntimeException::class)

        val measuredLocation = TestDataHelper.measuredLocation()
        tripRepository.addLocation(TestDataHelper.tripIdentifier, measuredLocation)
    }

    @Test
    fun canDeleteTrip() = runBlockingTest {
        val trip = TestDataHelper.trip()

        tripRepository.delete(trip)

        verify(tripDao).delete(trip)
    }

    @Test
    fun canDeleteAll() = runBlockingTest {
        tripRepository.deleteAll()

        verify(tripDao).deleteAll()
    }

}
