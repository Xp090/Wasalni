package com.starbugs.wasalni_core.data.source

import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Moshi
import com.starbugs.wasalni_core.BuildConfig
import com.starbugs.wasalni_core.data.model.RideRequest
import com.starbugs.wasalni_core.data.model.SentRideRequest
import com.starbugs.wasalni_core.data.model.Trip
import com.starbugs.wasalni_core.data.model.User
import com.starbugs.wasalni_core.util.socket.SocketEvent
import com.starbugs.wasalni_core.util.socket.SocketEventFactory
import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.OkHttpClient
import timber.log.Timber


class SocketConnection(private val moshi: Moshi, private val okHttpClient: OkHttpClient) {
    lateinit var socket: Socket
    lateinit var eventFactory: SocketEventFactory

    val riderFindDriverRequestEvent by lazy {
        eventFactory.createEmitterListenerWithPublishSubject<RideRequest, Trip>(SocketEvent.RiderFindDriverRequest)
    }
    val driverListenForRiderRequestEvent by lazy {
        eventFactory.createEmitterListenerWithBehaviorSubject<Boolean, SentRideRequest>(SocketEvent.DriverListenForRiderRequest)
    }

    val updateLocationEvent by lazy {eventFactory.createEmitter<LatLng>(SocketEvent.UpdateLocation)}


    fun initSocket(onConnected: () -> Unit) {
        if (!::socket.isInitialized) {
            IO.setDefaultOkHttpCallFactory(okHttpClient)
            IO.setDefaultOkHttpWebSocketFactory(okHttpClient)
            socket = IO.socket(BuildConfig.BASE_URL)
            eventFactory = SocketEventFactory(moshi, socket)
            socket.on(Socket.EVENT_CONNECT){
                Timber.d("Socket.Io ${socket.id()}")
                onConnected()
            }
        }
        socket.connect()


    }

    fun disconnectSocket() {
        socket.disconnect()
    }
    
}


