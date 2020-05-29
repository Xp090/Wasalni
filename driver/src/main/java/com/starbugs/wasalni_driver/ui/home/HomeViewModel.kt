package com.starbugs.wasalni_driver.ui.home

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.starbugs.wasalni_core.data.model.Trip
import com.starbugs.wasalni_core.data.repository.TripRepository
import com.starbugs.wasalni_core.ui.BaseViewModel
import com.starbugs.wasalni_core.util.`typealias`.StateLiveData
import com.starbugs.wasalni_core.util.ext.subscribeWithParsedError
import com.starbugs.wasalni_driver.data.repository.DriverTripRepository


class HomeViewModel(private val tripRepository: DriverTripRepository) : BaseViewModel() {
    val currentLocation = MutableLiveData<LatLng>()
    val currentTrip = StateLiveData<Trip>()


    init {
        launch {
            tripRepository.currentTrip.subscribeWithParsedError(currentTrip)
        }
        launch {
            tripRepository.currentLocation.subscribe {
                currentLocation.value = it
            }
        }
    }
}
