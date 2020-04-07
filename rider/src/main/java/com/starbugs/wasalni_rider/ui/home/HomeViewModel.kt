package com.starbugs.wasalni_rider.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.data.holder.TripStateHolder
import com.starbugs.wasalni_core.data.holder.TripStateLiveData
import com.starbugs.wasalni_core.data.model.TripEstimatedInfo
import com.starbugs.wasalni_core.data.model.RideRequest
import com.starbugs.wasalni_core.data.model.Trip
import com.starbugs.wasalni_core.data.model.User
import com.starbugs.wasalni_core.ui.BaseViewModel
import com.starbugs.wasalni_core.util.ext.schedule
import com.starbugs.wasalni_core.util.ext.subscribeWithParsedError
import com.starbugs.wasalni_core.util.livedata.StateLiveData
import com.starbugs.wasalni_driver.data.repository.RiderTripRepository
import timber.log.Timber


class HomeViewModel(private val tripRepository: RiderTripRepository) : BaseViewModel() {

    val tripUiState = TripStateLiveData()

    val currentLocation = tripRepository.currentLocation

    val rideRequest = MutableLiveData(RideRequest())

    val isLoading = MutableLiveData(false)

    val tripEstimatedInfo = MutableLiveData<TripEstimatedInfo>()

    val trip = StateLiveData<Trip>()


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

    fun getTripEstimatedInfo(): LiveData<NetworkState<TripEstimatedInfo>> {
        return launchWithNetworkLiveData { liveData ->
            tripRepository.getTripEstimatedInfo(
                rideRequest.value?.pickupPoint!!,
                rideRequest.value?.destinationPoint!!
            )
                .schedule()
                .subscribe { resp ->
                    liveData.value = resp
                    if (resp is NetworkState.Success) {
                        tripEstimatedInfo.value = resp.data
                    }
                }
        }


    }


    fun findDriver(request: RideRequest) {
        launch {
            tripRepository.findDriver(request)
                .schedule().subscribe({
                    when (it) {
                        is NetworkState.Success -> {
                            if (tripUiState.value is TripStateHolder.FindDriver) {
                                tripUiState.nextState()
                                trip.value = it
                                isLoading.value = false
                            } else {
                                trip.value = null
                            }
                        }
                        is NetworkState.Failure -> {
                            tripUiState.value = TripStateHolder.Init
                            isLoading.value = false
                            trip.value = null
                        }
                    }
                },{
                    Timber.e(it)
                })
        }
    }
}
