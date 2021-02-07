package mwvdev.berightthere.android.service

import com.fatboyindustrial.gsonjavatime.Converters
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import mwvdev.berightthere.android.TestDataHelper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


class BeRightThereServiceTest {

    private lateinit var gson: Gson

    private var server = MockWebServer()

    private lateinit var beRightThereService: BeRightThereService

    @Before
    fun setUp() {
        gson = Converters.registerOffsetDateTime(GsonBuilder()).create()

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        beRightThereService = retrofit.create()
    }

    @Test
    fun checkin() = runBlockingTest {
        server.enqueue(MockResponse().setBody(gson.toJson(TestDataHelper.checkinDto())))

        runBlocking {
            var checkinDto = beRightThereService.checkin()

            val recordedRequest = server.takeRequest()

            assertThat(checkinDto.identifier).isEqualTo(TestDataHelper.tripIdentifier)
            assertThat(recordedRequest.path).isEqualTo("/api/trip/checkin")
        }
    }

    @Test
    fun addLocation() = runBlockingTest {
        server.enqueue(MockResponse())

        val locationDto = TestDataHelper.locationDto()
        runBlocking { beRightThereService.addLocation(TestDataHelper.tripIdentifier, locationDto) }

        val recordedRequest = server.takeRequest()

        assertThat(recordedRequest.path).isEqualTo("/api/trip/${TestDataHelper.tripIdentifier}/addLocation")
        assertThat(recordedRequest.method).isEqualTo("POST")
        assertThat(recordedRequest.body.readUtf8()).isEqualTo(gson.toJson(locationDto))
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

}