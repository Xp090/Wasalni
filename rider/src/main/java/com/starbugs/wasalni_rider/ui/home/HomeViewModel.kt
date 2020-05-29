package com.starbugs.wasalni_rider.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.data.holder.TripStateLiveData
import com.starbugs.wasalni_core.data.model.TripEstimatedInfo
import com.starbugs.wasalni_core.data.model.RideRequest
import com.starbugs.wasalni_core.data.model.Trip
import com.starbugs.wasalni_core.ui.BaseViewModel
import com.starbugs.wasalni_core.util.ext.schedule
import com.starbugs.wasalni_core.util.`typealias`.StateLiveData
import com.starbugs.wasalni_core.util.ext.subscribeWithParsedError
import com.starbugs.wasalni_driver.data.repository.RiderTripRepository
import timber.log.Timber


class HomeViewModel(private val tripRepository: RiderTripRepository) : BaseViewModel() {

    val tripUiState = TripStateLiveData()

    val currentLocation = MutableLiveData<LatLng>()

    val rideRequest = MutableLiveData(RideRequest())

    val isLoading = MutableLiveData(false)

    val tripEstimatedInfo = MutableLiveData<TripEstimatedInfo>()

    val currentTrip = StateLiveData<Trip>()

    val driverLocation = StateLiveData<LatLng>(NetworkState.Initial())


    init {
        fetchCurrentTrip()

        launch {
            tripRepository.currentTrip.subscribeWithParsedError(currentTrip)
        }
        launch {
            tripRepository.currentLocation.subscribe {
                isLoading.value = false
                currentLocation.value = it
                listenForDriverLocation()
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

    fun fetchCurrentTrip(): StateLiveData<Trip> {
        isLoading.value = true
        launch {
            tripRepository.getCurrentTrip()
                .subscribe()
        }

        return currentTrip
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
        isLoading.value = true
        launch {
            tripRepository.findDriver(request)
                .schedule().subscribe({
                    isLoading.value = false
                },{
                    Timber.e(it)
                    isLoading.value = false
                })
        }
    }

    fun listenForDriverLocation() {
        launch {
            tripRepository.getDriverLocation()
                .subscribeWithParsedError(driverLocation)
        }
    }
}
