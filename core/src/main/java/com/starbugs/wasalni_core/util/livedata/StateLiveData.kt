package com.starbugs.wasalni_core.util.livedata

import androidx.lifecycle.MutableLiveData
import com.starbugs.wasalni_core.data.holder.NetworkState

typealias StateLiveData<T> = MutableLiveData<NetworkState<T>>

//class StateLiveData<T> : MutableLiveData<NetworkState<T>> {
//
//
//
//
//
//    constructor():super()
//
//    constructor(defaultValue: NetworkState<T>): super(defaultValue)
//
//    override fun setValue(value: NetworkState<T>?) {
//        super.setValue(value)
//        updateStatesLiveData(value)
//    }
//
//    override fun postValue(value: NetworkState<T>?) {
//        super.postValue(value)
//        updateStatesLiveData(value)
//    }
//    private fun updateStatesLiveData(value: NetworkState<T>?) {
//        if (value === null) {
//            return
//        }
//        when (value) {
//            is NetworkState.Success -> {
//                value.success.value = value.data
//                value.loading.value = false
//            }
//            is NetworkState.Failure -> {
//                value.failure.value = value.error
//                value.loading.value = false
//            }
//            is NetworkState.Loading ->  value.loading.value = true
//        }
//    }
//
//
//}