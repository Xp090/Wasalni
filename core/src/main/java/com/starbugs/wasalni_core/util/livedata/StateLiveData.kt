package com.starbugs.wasalni_core.util.livedata

import androidx.lifecycle.MutableLiveData
import com.starbugs.wasalni_core.data.holder.NetworkState

typealias StateLiveData<T> = MutableLiveData<NetworkState<T>>
