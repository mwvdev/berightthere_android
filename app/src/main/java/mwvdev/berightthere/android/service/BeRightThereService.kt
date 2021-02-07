package mwvdev.berightthere.android.service

import mwvdev.berightthere.android.dto.CheckinDto
import mwvdev.berightthere.android.dto.LocationDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BeRightThereService {

    @GET("/api/trip/checkin")
    suspend fun checkin(): CheckinDto

    @POST("/api/trip/{tripIdentifier}/addLocation")
    suspend fun addLocation(
        @Path("tripIdentifier") tripIdentifier: String,
        @Body location: LocationDto
    )

}

