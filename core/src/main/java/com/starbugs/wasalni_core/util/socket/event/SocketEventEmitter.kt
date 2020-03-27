package com.starbugs.wasalni_core.util.socket.event

import com.squareup.moshi.Moshi
import io.socket.client.Socket
import org.json.JSONObject

open class SocketEventEmitter<E>(
    protected val moshi: Moshi,
    protected val socket: Socket,
    protected var eventName: String,
    protected val emitValueType: Class<E>
) {

   open fun emitEvent(vararg args: Any) {
        socket.emit(eventName, args)
    }

   open fun emitEventObject(data: E) {
        val jsonAdapter = moshi.adapter<E>(emitValueType)
        val jsonObject = JSONObject(jsonAdapter.toJson(data))
        socket.emit(eventName, jsonObject)
    }


}