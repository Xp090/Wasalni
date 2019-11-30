package com.starbugs.wasalni_core.data.source

import com.starbugs.wasalni_core.data.model.TripEstimiatedInfo
import com.starbugs.wasalni_core.data.model.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WasalniTripApi {

    @GET("/trip/cost")
    fun getTripEstimiatedInfo(@Query("origin") origin: String,
                              @Query("destination") destination: String): Single<TripEstimiatedInfo>
}