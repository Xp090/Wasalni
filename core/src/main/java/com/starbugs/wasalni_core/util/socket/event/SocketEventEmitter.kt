package com.starbugs.wasalni_core.util.socket.event

import com.squareup.moshi.Moshi
import io.socket.client.Ack
import io.socket.client.Socket
import org.json.JSONObject
import timber.log.Timber

open class SocketEventEmitter<E>(
    protected val moshi: Moshi,
    protected val socket: Socket,
    protected var eventName: String,
    protected val emitValueType: Class<E>
) {

    open fun emitEvent(args: E, ackCallback: Ack? = null) {
        socket.emit(eventName, args, ackCallback)
    }

    open fun emitEventObject(data: E, ackCallback: Ack? = null) {
        val jsonAdapter = moshi.adapter(emitValueType)
        val jsonObject = JSONObject(jsonAdapter.toJson(data))
        socket.emit(eventName, jsonObject)
    }


}