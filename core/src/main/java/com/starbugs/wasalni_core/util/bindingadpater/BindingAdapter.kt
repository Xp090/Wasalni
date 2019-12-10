package com.starbugs.wasalni_core.util.bindingadpater

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.starbugs.wasalni_core.data.holder.NetworkState


@BindingAdapter("app:showWhenLoading")
fun <T> View.showWhenLoading( state: MutableLiveData<NetworkState<T>>) {
    visibility = when (state.value) {
        is NetworkState.Loading -> View.VISIBLE
        else -> View.INVISIBLE
    }

}

@BindingAdapter("app:hideWhenLoading")
fun <T> View.hideWhenLoading( state: MutableLiveData<NetworkState<T>>) {
    visibility = when (state.value) {
        is NetworkState.Loading -> View.INVISIBLE
        else -> View.VISIBLE
    }
}

@BindingAdapter("app:disableWhenLoading")
fun <T> View.disableWhenLoading( state: MutableLiveData<NetworkState<T>>) {
   isEnabled = state.value !is NetworkState.Loading
}
