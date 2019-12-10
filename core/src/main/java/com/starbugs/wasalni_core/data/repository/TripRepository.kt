package com.starbugs.wasalni_core.data.repository

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.data.model.TripEstimiatedInfo
import com.starbugs.wasalni_core.data.source.WasalniSocket
import com.starbugs.wasalni_core.data.source.WasalniTripApi
import com.starbugs.wasalni_core.util.GeoUtils
import com.starbugs.wasalni_core.util.ext.mapToNetworkState
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber
import java.util.concurrent.TimeUnit


class TripRepository (val wasalniSocket: WasalniSocket,
                      private val tripApi: WasalniTripApi,
                      private val geocoder: Geocoder) {

    val currentLocation = MutableLiveData<LatLng>()

    fun getDriverLocation() = wasalniSocket.driverLocationSubject

    fun getTripRequestResponse() = wasalniSocket.tripRequestResponseSubject

    fun getIncomingRequest() = wasalniSocket.incomingTripsRequests


    fun geocodeLocation(location: LatLng): Single<String> {
        return GeoUtils.geocode(geocoder,location)
    }

    fun getTripEstimatedInfo(origin: LatLng, destination: LatLng) :Single<NetworkState<TripEstimiatedInfo>> {
      return tripApi.getTripEstimiatedInfo("${origin.latitude},${origin.longitude}","${destination.latitude},${destination.longitude}")
            .mapToNetworkState()
    }

}