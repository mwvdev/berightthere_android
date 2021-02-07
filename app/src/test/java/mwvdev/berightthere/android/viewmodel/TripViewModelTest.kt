package mwvdev.berightthere.android.viewmodel

import androidx.lifecycle.LiveData
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import mwvdev.berightthere.android.TestDataHelper
import mwvdev.berightthere.android.persistence.entity.TripWithLocations
import mwvdev.berightthere.android.repository.ITripRepository
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TripViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var tripRepository: ITripRepository

    @Mock
    private lateinit var tripWithLocationsLiveData: LiveData<TripWithLocations>

    private lateinit var viewModel: TripViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = TripViewModel(tripRepository)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun canInitializeView() = runBlockingTest {
        whenever(tripRepository.getTripWithLocations(TestDataHelper.tripIdentifier)).thenReturn(tripWithLocationsLiveData)

        viewModel.initializeView(TestDataHelper.tripIdentifier)

        assertThat(viewModel.tripIdentifier).isEqualTo(TestDataHelper.tripIdentifier)
        assertThat(viewModel.tripWithLocations).isEqualTo(tripWithLocationsLiveData)
    }

    @Test
    fun canGetTripUrl() {
        val expectedTripUrl = "http://example.org"
        whenever(tripRepository.getTripUrl(TestDataHelper.tripIdentifier)).thenReturn(expectedTripUrl)
        viewModel.initializeView(TestDataHelper.tripIdentifier)

        val actualTripUrl = viewModel.getTripUrl()

        assertThat(actualTripUrl).isEqualTo(expectedTripUrl)
    }

}