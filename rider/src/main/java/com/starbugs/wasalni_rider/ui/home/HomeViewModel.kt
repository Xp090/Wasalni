package com.starbugs.wasalni_rider.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.starbugs.wasalni_core.data.holder.TripStateHolder
import com.starbugs.wasalni_core.data.holder.TripStateLiveData
import com.starbugs.wasalni_core.data.model.TripRequest
import com.starbugs.wasalni_core.data.repository.TripRepository
import com.starbugs.wasalni_core.ui.BaseViewModel
import com.starbugs.wasalni_core.util.ext.schedule
import com.xp090.azemaattendance.util.extra.SingleLiveEvent
import java.util.concurrent.TimeUnit


class HomeViewModel(private val tripRepository: TripRepository) : BaseViewModel() {

    val tripUiState = TripStateLiveData()

    val currentLocation = tripRepository.currentLocation

    val tripRequest = TripRequest()

    val isLoading = MutableLiveData<Boolean>(false)

    fun driverLocation(): LiveData<LatLng> {
        return launchWithLiveData { liveData ->
            tripRepository.getDriverLocation()
                .schedule()
                .subscribe { data ->
                    liveData.value = data
                }
        }
    }


    fun geocodeAddress(location: LatLng): LiveData<String> {
       isLoading.value = true
        return launchWithLiveData { liveData ->
            tripRepository.geocodeLocation(location)
                .schedule()
                .subscribe { address ->
                    liveData.value = address
                    isLoading.value = false
                }
        }
    }
}
