package com.starbugs.wasalni_core.util.`typealias`

import androidx.lifecycle.MutableLiveData
import com.starbugs.wasalni_core.data.holder.NetworkState
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

typealias StateLiveData<T> = MutableLiveData<NetworkState<T>>
typealias StateBehaviorSubject<T> = BehaviorSubject<NetworkState<T>>
typealias StatePublishSubject<T> = PublishSubject<NetworkState<T>>
typealias StateObservable<T> = Observable<NetworkState<T>>
typealias StateSingle<T> = Single<NetworkState<T>>


object StateSubject {
    fun <T> createBehaviorSubject(defaultValue: NetworkState<T>? = null):StateBehaviorSubject<T> {
        return if (defaultValue == null) BehaviorSubject.create() else BehaviorSubject.createDefault(defaultValue)
    }
    fun <T> createPublishSubject():StatePublishSubject<T> {
        return PublishSubject.create()
    }
}