package com.starbugs.wasalni_driver.data.repository

import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.data.model.RideRequest
import com.starbugs.wasalni_core.data.model.Trip
import com.starbugs.wasalni_core.data.model.TripEstimatedInfo
import com.starbugs.wasalni_core.data.repository.TripRepository
import com.starbugs.wasalni_core.data.repository.UserRepository
import com.starbugs.wasalni_core.data.source.SocketConnection
import com.starbugs.wasalni_core.data.source.TripApi
import io.reactivex.Single
import io.reactivex.subjects.Subject


class RiderTripRepository (socketConnection: SocketConnection,
                           userRepository: UserRepository,
                           tripApi: TripApi,
                           geocoder: Geocoder):TripRepository(socketConnection,userRepository,tripApi,geocoder) {

    fun getTripEstimatedInfo(origin: LatLng, destination: LatLng) : Single<NetworkState<TripEstimatedInfo>> {
        return tripApi.getTripEstimatedInfo("${origin.latitude},${origin.longitude}","${destination.latitude},${destination.longitude}")
    }

    fun findDriver(request: RideRequest): Single<NetworkState<Trip>> {
        return socketConnection.riderFindDriverRequestEvent
            .emitEventObjectThenListenOnce(request)
            .firstOrError()
            .doAfterSuccess { currentTrip.onNext(it)  }

    }

    fun getDriverLocation(): Subject<NetworkState<LatLng>> {
        return socketConnection.listenForDriverLocation.listen()
    }
}