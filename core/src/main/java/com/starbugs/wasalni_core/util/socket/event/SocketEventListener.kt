package com.starbugs.wasalni_core.util.socket.event

import com.squareup.moshi.Moshi
import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.util.exception.SocketErrorException
import com.starbugs.wasalni_core.util.ext.mapToNetworkState
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
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



    fun startListening(
        eventName: String = this.eventName,
        listenOnce: Boolean = false
    ): SocketEventListener<E,L> {
        stopListening(eventName)
        socket.on(eventName) {
            val input = it[0]
            try {
                if (input is String) {
                    eventSubject.onError(SocketErrorException(input))
                    return@on
                }
                val jsonAdapter = moshi.adapter<L>(listenValueType)
                val jsonObject = input as JSONObject
                val data: L? = jsonAdapter.fromJson(jsonObject.toString())
                eventSubject.onNext(NetworkState.Success(data!!))
            } catch (ex: Exception) {
                Timber.e(ex)
                eventSubject.onError(RuntimeException())//todo better handle
            }
            if (listenOnce) {
                stopListening(eventName)
            }

        }
        return this
    }

    fun startListeningAndObserve(
        eventName: String = this.eventName,
        listenOnce: Boolean = false
    ): Subject<NetworkState<L>> {
        return startListening(eventName,listenOnce).eventSubject
    }

    fun stopListening(eventName: String = this.eventName) {
        socket.off(eventName)
    }


    fun resetSubject() {
        eventSubject = when (eventSubject) {
            is BehaviorSubject<NetworkState<L>> -> BehaviorSubject.create<NetworkState<L>>()
            is PublishSubject<NetworkState<L>> -> PublishSubject.create<NetworkState<L>>()
            else -> throw Exception("Unsupported Subject Type")
        }
    }

    override fun emitEvent(vararg args: Any) {
        if (this::class.simpleName == SocketEventListener::class.simpleName) {
            throw Exception("You Can't emit events from SocketEventListener")
        } else {
            super.emitEvent(*args)
        }

    }

    override fun emitEventObject(data: E) {
        if (this::class.simpleName == SocketEventListener::class.simpleName) {
            throw Exception("You Can't emit events from SocketEventListener")
        } else {
            super.emitEventObject(data)
        }
    }

}