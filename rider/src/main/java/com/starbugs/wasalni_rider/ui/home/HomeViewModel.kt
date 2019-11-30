package com.starbugs.wasalni_rider.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.data.holder.TripStateLiveData
import com.starbugs.wasalni_core.data.model.TripEstimiatedInfo
import com.starbugs.wasalni_core.data.model.TripRequest
import com.starbugs.wasalni_core.data.repository.TripRepository
import com.starbugs.wasalni_core.ui.BaseViewModel
import com.starbugs.wasalni_core.util.ext.schedule


class HomeViewModel(private val tripRepository: TripRepository) : BaseViewModel() {

    val tripUiState = TripStateLiveData()

    val currentLocation = tripRepository.currentLocation

    val tripRequest = MutableLiveData<TripRequest>(TripRequest())

    val isLoading = MutableLiveData<Boolean>(false)

    val tripEstimatedInfo = MutableLiveData<TripEstimiatedInfo>()

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

    fun getTripEstimatedInfo() : LiveData<NetworkState<TripEstimiatedInfo>>{
        return launchWithNetworkLiveData {liveData ->
            tripRepository.getTripEstimatedInfo(tripRequest.value?.pickupPoint!!,tripRequest.value?.destinationPoint!!)
                .schedule()
                .subscribe { resp ->
                    liveData.value = resp
                    if(resp is NetworkState.Success){
                        tripEstimatedInfo.value = resp.data
                    }
                }
        }


    }
}
