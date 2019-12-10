package com.starbugs.wasalni_driver.ui

import androidx.lifecycle.MutableLiveData
import com.starbugs.wasalni_core.data.model.TripRequest
import com.starbugs.wasalni_core.data.model.User
import com.starbugs.wasalni_core.data.repository.TripRepository
import com.starbugs.wasalni_core.ui.BaseViewModel
import com.starbugs.wasalni_core.util.ext.schedule


class IncomingRequestViewModel(private val  tripRepository: TripRepository) : BaseViewModel() {
    val incomingRequestRider = MutableLiveData<TripRequest>(tripRepository.getIncomingRequest().value)

    fun accept() {
        tripRepository.wasalniSocket.respondToIncomingRequest(incomingRequestRider.value!!)
    }
    fun decline(){
        val resp = incomingRequestRider.value!!
        resp.driver = null
        tripRepository.wasalniSocket.respondToIncomingRequest(resp)
    }
}
