package com.starbugs.wasalni_core.data.model

import com.google.android.gms.maps.model.LatLng

class TripRequest(){
    var toPoint:LatLng? = null
    var toAddress:String? = null

    var fromPoint: LatLng? = null
    var fromAddress: String? = null

}