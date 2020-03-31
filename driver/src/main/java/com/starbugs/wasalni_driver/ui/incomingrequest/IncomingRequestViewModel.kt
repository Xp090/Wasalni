package com.starbugs.wasalni_driver.ui.incomingrequest

import androidx.lifecycle.MutableLiveData
import com.starbugs.wasalni_core.data.model.TripRequest
import com.starbugs.wasalni_core.data.repository.TripRepository
import com.starbugs.wasalni_core.ui.BaseViewModel
import com.starbugs.wasalni_driver.data.repository.DriverTripRepository


class IncomingRequestViewModel(private val  tripRepository: DriverTripRepository) : BaseViewModel() {
    val incomingRequestRider = tripRepository.currentTripRequest

    fun accept() {
        tripRepository.acceptRiderRequest()
    }
    fun decline(){
        tripRepository.declineRiderRequest()
    }
}
