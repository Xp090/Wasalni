package com.starbugs.wasalni_core.data.source

import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Moshi
import com.starbugs.wasalni_core.BuildConfig
import com.starbugs.wasalni_core.data.model.TripRequest
import com.starbugs.wasalni_core.data.model.User
import com.starbugs.wasalni_core.util.socket.SocketEvent
import com.starbugs.wasalni_core.util.socket.SocketEventFactory
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.OkHttpClient
import timber.log.Timber


class SocketConnection(private val moshi: Moshi, private val okHttpClient: OkHttpClient) {
    lateinit var socket: Socket
    lateinit var eventFactory: SocketEventFactory

//    val driverLocationEvent by lazy { eventFactory.createListenerWithBehaviorSubject<LatLng>(SocketEvent.DriverLocation) }
//    val tripRequestResponseEvent by lazy { eventFactory.createListenerWithBehaviorSubject<LatLng>(SocketEvent.DriverLocation) }
//    val incomingTripsRequestsEvent by lazy { eventFactory.createListenerWithBehaviorSubject<LatLng>(SocketEvent.DriverLocation) }

    val riderFindDriverRequestEvent by lazy {
        eventFactory.createEmitterListenerWithPublishSubject<TripRequest,User>(SocketEvent.RiderFindDriverRequest)
    }
    val driverListenForRiderRequestEvent by lazy {
        eventFactory.createEmitterListenerWithBehaviorSubject<Boolean,TripRequest>(SocketEvent.DriverListenForRiderRequest)
    }

    val updateLocationEvent by lazy {eventFactory.createEmitter<LatLng>(SocketEvent.UpdateLocation)}


    fun initSocket(onConnected: () -> Unit) {
        if (!::socket.isInitialized) {
            IO.setDefaultOkHttpCallFactory(okHttpClient)
            IO.setDefaultOkHttpWebSocketFactory(okHttpClient)
            socket = IO.socket(BuildConfig.BASE_URL)
            socket.on(Socket.EVENT_CONNECT){
                onConnected()
                Timber.d("Socket.Io ${socket.id()}")
            }
        }
        socket.connect()

//        val jsonAdapter = moshi.adapter<LatLng>(LatLng::class.java)
//        val jsonObject = JSONObject(jsonAdapter.toJson(LatLng(1.0,1.0)))
//        socket.emit("UpdateLocation",jsonObject)
//
//        socket.on("qwer") {
//            val ack = it[it.size - 1] as Ack
//            ack.call("hi")
//            ack.call("hi2")
//            ack.call("hi3")
//
//        }

//        socket.on("back"){
//            Timber.w(it.toString())
//        }

        eventFactory = SocketEventFactory(moshi, socket)
    }

    fun disconnectSocket() {
        socket.disconnect()
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


