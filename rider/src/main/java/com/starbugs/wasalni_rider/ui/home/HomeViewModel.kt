package com.starbugs.wasalni_rider.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.data.holder.TripStateHolder
import com.starbugs.wasalni_core.data.holder.TripStateLiveData
import com.starbugs.wasalni_core.data.model.TripEstimiatedInfo
import com.starbugs.wasalni_core.data.model.TripRequest
import com.starbugs.wasalni_core.data.model.User
import com.starbugs.wasalni_core.data.repository.TripRepository
import com.starbugs.wasalni_core.ui.BaseViewModel
import com.starbugs.wasalni_core.util.ext.schedule
import com.starbugs.wasalni_core.util.ext.subscribeWithParsedError
import com.starbugs.wasalni_core.util.livedata.StateLiveData
import com.starbugs.wasalni_driver.data.repository.RiderTripRepository


class HomeViewModel(private val tripRepository: RiderTripRepository) : BaseViewModel() {

    val tripUiState = TripStateLiveData()

    val currentLocation = tripRepository.currentLocation

    val tripRequest = MutableLiveData<TripRequest>(TripRequest())

    val isLoading = MutableLiveData<Boolean>(false)

    val tripEstimatedInfo = MutableLiveData<TripEstimiatedInfo>()

    val tripDriver = StateLiveData<User>()


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

    fun findDriver(request: TripRequest){
        launch {
            tripRepository.findDriver(request)
                .subscribeWithParsedError(tripDriver){
                    when (it) {
                        is NetworkState.Success -> {
                            if (tripUiState.value is TripStateHolder.FindDriver){
                                tripUiState.nextState()
                                isLoading.value = false
                            }else {
                                tripDriver.value = null
                            }
                        }
                        is NetworkState.Failure -> {
                            tripUiState.value = TripStateHolder.Init
                            isLoading.value = false
                            tripDriver.value = null
                        }
                    }
                }
        }
    }
}
