package com.starbugs.wasalni_core.util.retrofit

import com.starbugs.wasalni_core.data.holder.NetworkState
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.internal.operators.observable.ObservableSingleSingle
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type

@Suppress("UNCHECKED_CAST")
class RxCallFactoryForNetworkState : CallAdapter.Factory() {

    override fun get(
        returnType: Type?,
        annotations: Array<out Annotation>?,
        retrofit: Retrofit?
    ): CallAdapter<*, *>? {
        val rxAdapterFactory = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
        val rxAdapter = rxAdapterFactory.get(returnType!!, annotations!!, retrofit!!)
        return RxCallForNetworkState(rxAdapter as CallAdapter<*, Any>)
    }


}

@Suppress("UNCHECKED_CAST")
class RxCallForNetworkState<R>(
    private val rxAdapter: CallAdapter<R, Any>
) : CallAdapter<R, Any> {

    override fun responseType(): Type = rxAdapter.responseType()

    override fun adapt(call: Call<R>): Any {
        return when (val adaptedObservable = rxAdapter.adapt(call)) {
            is Observable<*> -> (adaptedObservable as Observable<NetworkState<Any>>)
                .onErrorReturn { NetworkState.failureFromThrowable(it) }
            is Single<*> -> (adaptedObservable as Single<NetworkState<Any>>)
                .onErrorReturn { NetworkState.failureFromThrowable(it) }
            is Flowable<*> -> (adaptedObservable as Flowable<NetworkState<Any>>)
                .onErrorReturn { NetworkState.failureFromThrowable(it) }
            is Maybe<*> -> (adaptedObservable as Maybe<NetworkState<Any>>)
                .onErrorReturn { NetworkState.failureFromThrowable(it) }
            else -> adaptedObservable
        }
    }
}