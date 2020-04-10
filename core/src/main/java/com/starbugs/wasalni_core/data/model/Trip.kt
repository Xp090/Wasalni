package com.starbugs.wasalni_core.data.model

import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class RideRequest(
    @Json(name = "pickupPoint")
    var pickupPoint: LatLng = LatLng(0.0, 0.0),
    @Json(name = "pickupAddress")
    var pickupAddress: String = "",
    @Json(name = "destinationPoint")
    var destinationPoint: LatLng = LatLng(0.0, 0.0),
    @Json(name = "destinationAddress")
    var destinationAddress: String = ""
)

@JsonClass(generateAdapter = true)
data class SentRideRequest(
    @Json(name = "id")
    val id: String,
    @Json(name = "rider")
    val rider: User,
    @Json(name = "pickupPoint")
    val pickupPoint: LatLng,
    @Json(name = "pickupAddress")
    val pickupAddress: String,
    @Json(name = "destinationPoint")
    val destinationPoint: LatLng,
    @Json(name = "destinationAddress")
    val destinationAddress: String,
    @Json(name = "driver")
    val driver: User?,
    @Json(name = "requestStatus")
    val requestStatus: String
)

/*
 requestsSent: Map<String,SentRideRequest>
    requestStatus: RideDriverResponse
    driver: mongoose.Types.ObjectId | DriverDocument
 */
@JsonClass(generateAdapter = true)
data class Trip(
    @Json(name = "id")
    val id: String,
    @Json(name = "rideRequest")
    val rideRequest: SentRideRequest,
    @Json(name = "tripStatus")
    val tripStatus: String
) {
    val rider: User = rideRequest.rider
    val driver: User = rideRequest.driver!!
    val pickupPoint: LatLng = rideRequest.pickupPoint
    val pickupAddress: String = rideRequest.pickupAddress
    val destinationPoint: LatLng = rideRequest.destinationPoint
    val destinationAddress: String = rideRequest.destinationAddress
}


@JsonClass(generateAdapter = true)
data class TripEstimatedInfo(
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

object TripStatus {
    const val DriverOnTheWayForPickUpPoint = "DriverOnTheWayForPickUpPoint"
    const val DriverArrivedAtPickUpPoint = "DriverOnTheWayForPickUpPoint"
    const val TripCanceledByRider = "TripCanceledByRider"
    const val TripCanceledByDriver = "TripCanceledByDriver"
    const val TripOngoing = "TripOngoing"
    const val TripEnded = "TripEnded"
}