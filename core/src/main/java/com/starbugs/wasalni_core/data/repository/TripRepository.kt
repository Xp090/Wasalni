package com.starbugs.wasalni_core.data.repository

import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.data.model.TripEstimatedInfo
import com.starbugs.wasalni_core.data.model.RideRequest
import com.starbugs.wasalni_core.data.model.Trip
import com.starbugs.wasalni_core.data.source.SocketConnection
import com.starbugs.wasalni_core.data.source.TripApi
import com.starbugs.wasalni_core.util.GeoUtils
import com.starbugs.wasalni_core.util.ext.mapToNetworkState
import io.reactivex.Single


abstract class TripRepository (val socketConnection: SocketConnection,
                      private val userRepository: UserRepository,
                      private val tripApi: TripApi,
                      private val geocoder: Geocoder) {

    val currentLocation = MutableLiveData<LatLng>()

    fun initSocketConnection(onConnected: () -> Unit) = socketConnection.initSocket(onConnected)

    fun updateLocation(latLng: LatLng) {
        currentLocation.value = latLng
        socketConnection.updateLocationEvent.emitEventObject(latLng)
    }


    fun geocodeLocation(location: LatLng): Single<String> {
        return GeoUtils.geocode(geocoder,location)
    }

    fun getTripEstimatedInfo(origin: LatLng, destination: LatLng) :Single<NetworkState<TripEstimatedInfo>> {
      return tripApi.getTripEstimatedInfo("${origin.latitude},${origin.longitude}","${destination.latitude},${destination.longitude}")
    }

    fun findDriver(request: RideRequest): Single<NetworkState<Trip>> {
      return socketConnection.riderFindDriverRequestEvent
            .emitEventObjectThenListenOnce(request)
            .firstOrError()

    }
    fun disconnectSocket() = socketConnection.disconnectSocket()

}