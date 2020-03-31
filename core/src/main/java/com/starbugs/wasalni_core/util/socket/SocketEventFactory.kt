package com.starbugs.wasalni_core.util.socket

import com.squareup.moshi.Moshi
import com.starbugs.wasalni_core.util.socket.event.SocketEventEmitter
import com.starbugs.wasalni_core.util.socket.event.SocketEventEmitterListener
import com.starbugs.wasalni_core.util.socket.event.SocketEventListener
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.socket.client.Socket

class SocketEventFactory(val moshi: Moshi, val socket: Socket) {

    inline fun <reified E> createEmitter(eventName: String): SocketEventEmitter<E> {
        return SocketEventEmitter(
            moshi,
            socket,
            eventName,
            E::class.java
        )
    }

    inline fun <reified L> createListenerWithBehaviorSubject(eventName: String): SocketEventListener<Nothing,L> {
        return SocketEventListener(
            moshi,
            socket,
            eventName,
            Nothing::class.java,
            L::class.java,
            BehaviorSubject.create()
        )
    }

    inline fun <reified L>  createListenerWithPublishSubject(eventName: String): SocketEventListener<Nothing,L> {
        return SocketEventListener(
            moshi,
            socket,
            eventName,
            Nothing::class.java,
            L::class.java,
            PublishSubject.create()
        )
    }

    inline fun <reified E,reified L> createEmitterListenerWithBehaviorSubject(eventName: String): SocketEventEmitterListener<E,L> {
        return SocketEventEmitterListener(
            moshi,
            socket,
            eventName,
            E::class.java,
            L::class.java,
            BehaviorSubject.create()
        )
    }

    inline fun <reified E, reified L> createEmitterListenerWithPublishSubject(eventName: String): SocketEventEmitterListener<E,L> {
        return SocketEventEmitterListener(
            moshi,
            socket,
            eventName,
            E::class.java,
            L::class.java,
            PublishSubject.create()
        )
    }

}