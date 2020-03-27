package com.starbugs.wasalni_driver.ui

import androidx.lifecycle.MutableLiveData
import com.starbugs.wasalni_core.data.model.TripRequest
import com.starbugs.wasalni_core.data.repository.TripRepository
import com.starbugs.wasalni_core.ui.BaseViewModel


class IncomingRequestViewModel(private val  tripRepository: TripRepository) : BaseViewModel() {
    val incomingRequestRider = MutableLiveData<TripRequest>(tripRepository.getIncomingRequest().value)

    fun accept() {
        tripRepository.socketConnection.respondToIncomingRequest(incomingRequestRider.value!!)
    }
    fun decline(){
        val resp = incomingRequestRider.value!!
        resp.driver = null
        tripRepository.socketConnection.respondToIncomingRequest(resp)
    }
}
