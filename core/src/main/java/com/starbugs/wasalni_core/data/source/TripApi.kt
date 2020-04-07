package com.starbugs.wasalni_core.data.source

import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.data.model.TripEstimatedInfo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TripApi {

    @GET("/trip/cost")
    fun getTripEstimatedInfo(@Query("origin") origin: String,
                             @Query("destination") destination: String): Single<NetworkState<TripEstimatedInfo>>
}