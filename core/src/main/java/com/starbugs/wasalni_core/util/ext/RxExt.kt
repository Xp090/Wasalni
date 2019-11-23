@file:Suppress("UNCHECKED_CAST")

package com.starbugs.wasalni_core.util.ext


import androidx.lifecycle.MutableLiveData
import com.starbugs.wasalni_core.data.holder.NetworkState
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject

/**
 * Use SchedulerProvider configuration for Single
 */
fun <T> Single<T>.schedule(): Single<T> =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun Completable.schedule(): Completable =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Subject<T>.schedule(): Observable<T> =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.schedule(): Observable<T> =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.mapToNetworkState (): Single<NetworkState<T>> = map { NetworkState.Success(it) }

fun <T> Subject<T>.mapToNetworkState (): Observable<NetworkState<T>> = map { NetworkState.Success(it) }

fun <T> Single<NetworkState<T>>.subscribeWithParsedError(liveData: MutableLiveData<NetworkState<T>>): Disposable {
    return schedule().subscribe({
        liveData.value = it
    }, {
        liveData.value = NetworkState.failureFromThrowable(it)
    })
}

fun <T> Subject<NetworkState<T>>.subscribeWithParsedError(liveData: MutableLiveData<NetworkState<T>>): Disposable {
    return schedule().subscribe({
        liveData.value = it
    }, {
        liveData.value = NetworkState.failureFromThrowable(it)
    })
}