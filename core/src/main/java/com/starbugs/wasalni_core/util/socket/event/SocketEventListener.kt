package com.starbugs.wasalni_core.util.socket.event

import com.squareup.moshi.Moshi
import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.util.exception.SocketErrorException
import com.starbugs.wasalni_core.util.ext.mapToNetworkState
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import io.socket.client.Ack
import io.socket.client.Socket
import org.json.JSONObject
import timber.log.Timber
import java.lang.RuntimeException

open class SocketEventListener<E,L>(
    moshi: Moshi,
    socket: Socket,
    eventName: String,
    emitValueType: Class<E>,
    private val listenValueType: Class<L>,
    var eventSubject: Subject<NetworkState<L>>
) : SocketEventEmitter<E>(moshi, socket, eventName, emitValueType) {



//   protected fun startListening(
//        eventName: String = this.eventName): SocketEventListener<E,L> {
//        stopListening(eventName)
//        socket.on(eventName) {
//            handleIncomingInput(it)
//        }
//        return this
//    }

    @Suppress("UNCHECKED_CAST")
    protected open fun handleIncomingInput (inputArgs: Array<Any>) {
        val input = inputArgs[0]
        try {
            if (input is String) {
                eventSubject.onNext(NetworkState.failureFromThrowable(SocketErrorException(input)) )
                return
            }else if (listenValueType.isInstance(input)) {
                eventSubject.onNext(NetworkState.Success(input as L))
            }
            val jsonAdapter = moshi.adapter(listenValueType)
            val jsonObject = input as JSONObject
            val data: L? = jsonAdapter.fromJson(jsonObject.toString())
            eventSubject.onNext(NetworkState.Success(data!!))
        } catch (ex: Exception) {
            Timber.e(ex)
            eventSubject.onError(RuntimeException())//todo better handle
        }

    }
    fun listen(
        eventName: String = this.eventName
    ): Subject<NetworkState<L>> {
   //     stopListening(eventName)
        Timber.w("Socket.Io: $eventName ${socket.id()}")
        socket.on(eventName) {
            handleIncomingInput(it)
        }
        return eventSubject
    }

    fun listenOnce(
        eventName: String = this.eventName
    ): Subject<NetworkState<L>> {
        resetSubject()
       // stopListening(eventName)
        socket.once(eventName) {
            handleIncomingInput(it)
        }
        return eventSubject
    }

    fun stopListening(eventName: String = this.eventName) {
        socket.off(eventName)
    }


    fun resetSubject() {
        eventSubject.onComplete()
        if (!eventSubject.hasComplete()) eventSubject.onComplete()
        eventSubject = when (eventSubject) {
            is BehaviorSubject<NetworkState<L>> -> BehaviorSubject.create()
            is PublishSubject<NetworkState<L>> -> PublishSubject.create()
            else -> throw Exception("Unsupported Subject Type")
        }
    }

    override fun emitEvent(args: E, ackCallback: Ack?) {
        if (this::class.simpleName == SocketEventListener::class.simpleName) {
            throw Exception("You Can't emit events from SocketEventListener")
        } else {
            super.emitEvent(args,ackCallback)
        }

    }

    override fun emitEventObject(data: E, ackCallback: Ack?) {
        if (this::class.simpleName == SocketEventListener::class.simpleName) {
            throw Exception("You Can't emit events from SocketEventListener")
        } else {
            super.emitEventObject(data, ackCallback)
        }
    }

}