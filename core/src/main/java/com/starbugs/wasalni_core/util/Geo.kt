package com.starbugs.wasalni_core.util

import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import timber.log.Timber
import java.util.concurrent.TimeUnit


object GeoUtils {

//    private val geocodeObservable = PublishSubject.create<String>()

    fun geocode(geocoder: Geocoder, location:LatLng): Single<String> {
        return Single.create {emitter ->
            try {
                val addresses = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1)
                if (addresses.isEmpty()){
                    emitter.onSuccess("Unknown Location")
                }else{
                    val address = addresses[0].getAddressLine(0)
                    emitter.onSuccess(address)
                }
            } catch (ex: Exception) {
                Timber.e(ex)
                emitter.onSuccess("Unknown Location")
            }
        }

    }
}