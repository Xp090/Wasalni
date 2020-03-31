package com.starbugs.wasalni_core.util.socket.event

import com.squareup.moshi.Moshi
import com.starbugs.wasalni_core.data.holder.NetworkState
import io.reactivex.subjects.Subject
import io.socket.client.Ack
import io.socket.client.Socket
import timber.log.Timber

open class SocketEventEmitterListener<E,L>(
    moshi: Moshi,
    socket: Socket,
    eventName: String,
    emitValueType: Class<E>,
    listenValueType: Class<L>,
    eventSubject: Subject<NetworkState<L>>
): SocketEventListener<E,L>(moshi,socket,eventName,emitValueType,listenValueType,eventSubject) {

    private val defaultCallback: (L) -> Unit = {
        throw RuntimeException("No Callback Received") //todo change it to not throw later on development
    }
    var callback: (L) -> Unit = defaultCallback

    fun emitEventThenListen(args: E): Subject<NetworkState<L>> {
        emitEvent(args)
        return listen()
    }

    fun emitEventObjectThenListen(data: E): Subject<NetworkState<L>> {
        emitEventObject(data)
        return listen()
    }

    fun emitEventThenListenOnce(args: E): Subject<NetworkState<L>> {
        emitEvent(args, Ack {
            handleIncomingInput(it)
        })
        return eventSubject
    }

    fun emitEventObjectThenListenOnce(data: E): Subject<NetworkState<L>> {
        emitEventObject(data, Ack {
            handleIncomingInput(it)
        })
        return eventSubject
    }

    override fun handleIncomingInput(inputArgs: Array<Any>) {
        super.handleIncomingInput(inputArgs)
        val ackArg = inputArgs[inputArgs.size - 1]
        callback = if(ackArg is Ack) {
            { ackArg.call(it) }
        } else {
            defaultCallback
        }

    }



}