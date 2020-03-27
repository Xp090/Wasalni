package com.starbugs.wasalni_core.util.socket.event

import com.squareup.moshi.Moshi
import com.starbugs.wasalni_core.data.holder.NetworkState
import io.reactivex.subjects.Subject
import io.socket.client.Socket

open class SocketEventEmitterListener<E,L>(
    moshi: Moshi,
    socket: Socket,
    eventName: String,
    emitValueType: Class<E>,
    listenValueType: Class<L>,
    eventSubject: Subject<NetworkState<L>>
): SocketEventListener<E,L>(moshi,socket,eventName,emitValueType,listenValueType,eventSubject) {


    fun emitEventThenListen(vararg args: Any, listenOnce: Boolean = false): Subject<NetworkState<L>> {
        emitEvent(args)
        return startListeningAndObserve(listenOnce =  listenOnce)
    }

    fun emitEventObjectThenListen(data: E, listenOnce: Boolean = false): Subject<NetworkState<L>> {
        emitEventObject(data)
        return startListeningAndObserve(listenOnce = listenOnce)
    }


}