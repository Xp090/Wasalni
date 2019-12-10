package com.starbugs.wasalni_core.data.model

import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


class TripRequest {
    var destinationPoint:LatLng? = null
    var destinationAddress:String? = null

    var pickupPoint: LatLng? = null
    var pickupAddress: String? = null

    var driver: User? = null
    var rider: User? = null

}

@JsonClass(generateAdapter = true)
data class TripEstimiatedInfo(
    @Json(name = "tripDirections")
    val tripDirections: TripDirections,
    @Json(name = "tripEconomy")
    val tripEconomy: TripEconomy
)

@JsonClass(generateAdapter = true)
data class TripDirections(
    @Json(name = "distance")
    val distance: Distance,
    @Json(name = "duration")
    val duration: Duration,
    @Json(name = "polylines")
    val polylines: List<String>
)

@JsonClass(generateAdapter = true)
data class Distance(
    @Json(name = "text")
    val text: String,
    @Json(name = "value")
    val value: Int
)

@JsonClass(generateAdapter = true)
data class Duration(
    @Json(name = "text")
    val text: String,
    @Json(name = "value")
    val value: Int
)

@JsonClass(generateAdapter = true)
data class TripEconomy(
    @Json(name = "cost")
    val cost: Double
)