package com.starbugs.wasalni_core.data.repository

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.starbugs.wasalni_core.data.source.WasalniSocket
import com.starbugs.wasalni_core.util.GeoUtils
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber
import java.util.concurrent.TimeUnit


class TripRepository (private val wasalniSocket: WasalniSocket,
                      private val geocoder: Geocoder) {

    val currentLocation = MutableLiveData<LatLng>()

    fun getDriverLocation() = wasalniSocket.driverLocationSubject

    fun geocodeLocation(location: LatLng): Single<String> {
        return GeoUtils.geocode(geocoder,location)
    }

}