package com.starbugs.wasalni_driver.data.repository

import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.data.model.RideRequest
import com.starbugs.wasalni_core.data.model.SentRideRequest
import com.starbugs.wasalni_core.data.repository.TripRepository
import com.starbugs.wasalni_core.data.repository.UserRepository
import com.starbugs.wasalni_core.data.source.SocketConnection
import com.starbugs.wasalni_core.data.source.TripApi
import com.starbugs.wasalni_core.util.`typealias`.StateSubject
import io.reactivex.Observable


class DriverTripRepository(
    socketConnection: SocketConnection,
    userRepository: UserRepository,
    tripApi: TripApi,
    geocoder: Geocoder
) : TripRepository(socketConnection, userRepository, tripApi, geocoder) {


    val currentTripRequest = StateSubject.createBehaviorSubject<SentRideRequest>(NetworkState.Initial())


    fun listenForRiderRequest(): Observable<NetworkState<SentRideRequest>> {
        return socketConnection.driverListenForRiderRequestEvent.listen()
            .doOnNext { currentTripRequest.onNext(it) }
    }

    fun acceptRiderRequest() {
        socketConnection.driverListenForRiderRequestEvent.callback(true)
        //todo currentTripRequest.value = NetworkState.Loading() +++ then verfiy the request
    }

    fun declineRiderRequest() {
        socketConnection.driverListenForRiderRequestEvent.callback(false)
        currentTripRequest.onNext(NetworkState.Initial())

    }
}