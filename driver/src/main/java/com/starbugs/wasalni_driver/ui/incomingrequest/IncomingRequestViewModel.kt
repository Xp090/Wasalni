package com.starbugs.wasalni_driver.ui.incomingrequest

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
