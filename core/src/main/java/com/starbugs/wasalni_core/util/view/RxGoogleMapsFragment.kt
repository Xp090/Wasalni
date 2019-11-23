package com.starbugs.wasalni_core.util.view

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.starbugs.wasalni_core.util.ext.schedule
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class RxMapsFragment : SupportMapFragment(){
    private lateinit var rxGoogleMap: RxGoogleMap

    override fun onDestroy() {
        super.onDestroy()
        if(::rxGoogleMap.isInitialized){
            rxGoogleMap.onFragmentDestroyed()
        }
    }

     fun getRxMapAsync(callback: OnRxMapReadyCallback) {
        val tempCallback = OnMapReadyCallback {
            rxGoogleMap = RxGoogleMap(it)
            callback.onMapReady(rxGoogleMap)
        }
        super.getMapAsync(tempCallback)
    }
}

class RxGoogleMap (val mapInstance: GoogleMap){

    private val disposables = CompositeDisposable()

    fun onCameraIdleListenerWithDebounce(debounceMS: Long, listener: (location: LatLng) -> Unit){
        mapInstance.setOnCameraIdleListener(object: GoogleMap.OnCameraIdleListener {
            val subject = PublishSubject.create<LatLng>()
            init {
                val subscription =  subject.debounce(debounceMS, TimeUnit.MILLISECONDS)
                    .schedule()
                    .subscribe(listener)
                disposables.add(subscription)
            }
            override fun onCameraIdle() {
                subject.onNext(mapInstance.cameraPosition.target)

            }
        })
    }

    fun onFragmentDestroyed(){
        disposables.clear()
    }


}

interface OnRxMapReadyCallback{
     fun onMapReady(rxMap: RxGoogleMap) {

    }
}