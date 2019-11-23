package com.starbugs.wasalni_core.ui

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    private val disposables = CompositeDisposable()

    fun launch(job: () -> Disposable) {
        disposables.add(job())
    }
    fun <T> launchWithLiveData(job: (liveData: MutableLiveData<T>) -> Disposable): LiveData<T> {
        val liveData: MutableLiveData<T> = MutableLiveData()
        disposables.add(job(liveData))
        return liveData
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}