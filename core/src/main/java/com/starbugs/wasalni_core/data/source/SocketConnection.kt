package com.starbugs.wasalni_core.data.source

import com.google.android.gms.maps.model.LatLng
import com.starbugs.wasalni_core.BuildConfig
import io.socket.client.IO
import io.socket.client.Socket
import com.squareup.moshi.Moshi
import com.starbugs.wasalni_core.data.model.TripRequest
import com.starbugs.wasalni_core.data.model.User
import com.starbugs.wasalni_core.util.socket.SocketEvent
import com.starbugs.wasalni_core.util.socket.SocketEventFactory
import okhttp3.OkHttpClient


class SocketConnection(private val moshi: Moshi, private val okHttpClient: OkHttpClient) {
    lateinit var socket: Socket
    lateinit var eventFactory: SocketEventFactory

//    val driverLocationEvent by lazy { eventFactory.createListenerWithBehaviorSubject<LatLng>(SocketEvent.DriverLocation) }
//    val tripRequestResponseEvent by lazy { eventFactory.createListenerWithBehaviorSubject<LatLng>(SocketEvent.DriverLocation) }
//    val incomingTripsRequestsEvent by lazy { eventFactory.createListenerWithBehaviorSubject<LatLng>(SocketEvent.DriverLocation) }

    val findDriverRequestEvent by lazy {
        eventFactory.createEmitterListenerWithPublishSubject<TripRequest,User>(SocketEvent.RiderFindDriverRequest)
    }

    val updateLocationEvent by lazy {eventFactory.createEmitter<LatLng>(SocketEvent.UpdateLocation)}



    fun initSocket(userId: String) {
        IO.setDefaultOkHttpCallFactory(okHttpClient)
        IO.setDefaultOkHttpWebSocketFactory(okHttpClient)
        socket = IO.socket(BuildConfig.BASE_URL)
        socket.connect()
        initUser(userId)
        eventFactory = SocketEventFactory(moshi,socket)
    }

    fun disconnectSocket() {
        socket.disconnect()
        socket.off()
    }

   private fun initUser(userId: String) {
       socket.emit(SocketEvent.InitUser, userId)
    }

//    fun updateLocation(latLng: LatLng) {
//        sendEventObject(SocketEvent.UpdateLocation, latLng)
//    }

//    fun listenDriverLocation() {
//        pipeToSubject(SocketEvent.DriverLocation,driverLocationSubject)
//    }
//    fun findDriver(tripRequest: TripRequest): Subject<User> {
//        sendEventObject(SocketEvent.FindDriverRequestFromRider,tripRequest)
//      return pipeToSubject(SocketEvent.FindDriverResponseToRider,tripRequestResponseSubject)
//    }
//    fun listenToIncomingRequests(): Subject<TripRequest> {
//        return pipeToSubject(SocketEvent.FindDriverRequestToDriver,incomingTripsRequests)
//    }
//    fun respondToIncomingRequest(tripRequest: TripRequest){
//        sendEventObject(SocketEvent.FindDriverResponseFromDriver,tripRequest)
//    }

}


