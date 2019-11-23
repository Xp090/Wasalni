package com.starbugs.wasalni_core.data.model

import com.google.android.gms.maps.model.LatLng

class TripRequest(){
    var destinationPoint:LatLng? = null
    var destinationAddress:String? = null

    var pickupPoint: LatLng? = null
    var pickupAddress: String? = null

}