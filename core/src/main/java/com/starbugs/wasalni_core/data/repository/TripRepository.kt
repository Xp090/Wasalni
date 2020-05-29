package com.starbugs.wasalni_core.data.repository

import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.data.model.Trip
import com.starbugs.wasalni_core.data.source.SocketConnection
import com.starbugs.wasalni_core.data.source.TripApi
import com.starbugs.wasalni_core.util.GeoUtils
import com.starbugs.wasalni_core.util.`typealias`.StateBehaviorSubject
import com.starbugs.wasalni_core.util.`typealias`.StateLiveData
import com.starbugs.wasalni_core.util.`typealias`.StateSubject
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject


abstract class TripRepository(
    val socketConnection: SocketConnection,
    protected val userRepository: UserRepository,
    protected val tripApi: TripApi,
    protected val geocoder: Geocoder
) {

    val currentLocation = BehaviorSubject.create<LatLng>()
    val currentTrip = StateSubject.createBehaviorSubject<Trip>()


    fun getCurrentTrip(): Single<NetworkState<Trip>> {
        currentTrip.onNext(NetworkState.Loading())
        return tripApi.getCurrentTrip()
            .doAfterSuccess {
                currentTrip.onNext(it)
            }
    }

    fun initSocketConnection(onConnected: () -> Unit) = socketConnection.initSocket(onConnected)

    fun updateLocation(latLng: LatLng) {
        currentLocation.onNext(latLng)
        socketConnection.updateLocationEvent.emitEventObject(latLng)
    }


    fun geocodeLocation(location: LatLng): Single<String> {
        return GeoUtils.geocode(geocoder, location)
    }


    fun disconnectSocket() = socketConnection.disconnectSocket()

}