package com.starbugs.wasalni_core.data.source

import com.google.android.gms.maps.model.LatLng
import com.starbugs.wasalni_core.BuildConfig
import io.socket.client.IO
import io.socket.client.Socket
import com.squareup.moshi.Moshi
import com.starbugs.wasalni_core.data.model.User
import com.starbugs.wasalni_core.util.exception.SocketErrorExcpetion
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.lang.RuntimeException


class WasalniSocket(private val moshi: Moshi, private val okHttpClient: OkHttpClient) {
    lateinit var socket: Socket

    val driverLocationSubject: BehaviorSubject<LatLng> = BehaviorSubject.create()
    val findDriverSubject: BehaviorSubject<User> = BehaviorSubject.create()


    fun initSocket(userId: String) {
        IO.setDefaultOkHttpCallFactory(okHttpClient)
        IO.setDefaultOkHttpWebSocketFactory(okHttpClient)
        socket = IO.socket(BuildConfig.BASE_URL)
        socket.connect()
        initUser(userId)
    }

    fun disconnectSocket() {
        socket.disconnect()
        socket.off()
    }

    fun initUser(userId: String) {
        sendEvent(SocketEvent.InitUser, userId)
    }

    fun updateLocation(lngLat: LatLng) {
        sendEventObject(SocketEvent.UpdateLocation, lngLat)
    }

    fun listenDriverLocation() {
        pipeToSubject(SocketEvent.DriverLocation,driverLocationSubject)
    }
    fun findDriver(){
        sendEvent(SocketEvent.FindDriverRequest)
    }

    private fun sendEvent(eventName: String, vararg args: Any) {
        socket.emit(eventName, args)
    }

    private inline fun <reified T> sendEventObject(eventName: String, data: T) {
        val jsonAdapter = moshi.adapter<T>(T::class.java)
        val jsonObject = JSONObject(jsonAdapter.toJson(data))
        socket.emit(eventName, jsonObject)
    }

    private inline fun <reified T> pipeToSubject(eventName: String,subject: Subject<T>): Subject<T> {
        socket.on(eventName){
            val input = it[0]
            try {
                val jsonAdapter = moshi.adapter<T>(T::class.java).failOnUnknown()
                val jsonObject = input as JSONObject
                val data:T? = jsonAdapter.fromJson(jsonObject.toString())
                subject.onNext(data!!)
            } catch (ex: Exception) {
                if (input is String){
                    subject.onError(SocketErrorExcpetion(input))
                }else{
                    subject.onError(RuntimeException())
                }
            }

        }
        return subject
    }
}


object SocketEvent {
    const val InitUser = "init_user"
    const val UpdateLocation = "UpdateLocation"
    const val DriverLocation = "DriverLocation"
    const val FindDriverRequest = "FindDriverRequest"
    const val FindDriverResponse ="FindDriverResponse"
}


fun <T> Socket.onEventWithArgs(eventName: String, listener: (data: Array<T?>) -> Unit) {
    on(eventName) {
        listener(it as Array<T?>)
    }
}

fun <T> Socket.onEventWithPrimitive(eventName: String, listener: (data: T?) -> Unit) {
    on(eventName) {
        listener(it[0] as T?)
    }
}


inline fun <reified T> Socket.onEventWithObject(
    eventName: String,
    crossinline listener: (data: T?) -> Unit
) {
    val observable: Observable<T> = Observable.create {

    }
    on(eventName) {
        val jsonObj = it[0] as JSONObject
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter<T>(T::class.java)
        val data: T? = jsonAdapter.fromJson(jsonObj.toString())
        listener(data)
    }
}