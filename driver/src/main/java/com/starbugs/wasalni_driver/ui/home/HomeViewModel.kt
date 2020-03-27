package com.starbugs.wasalni_driver.ui.home

import com.starbugs.wasalni_core.data.repository.TripRepository
import com.starbugs.wasalni_core.ui.BaseViewModel


class HomeViewModel(private val tripRepository: TripRepository) : BaseViewModel() {
    val currentLocation = tripRepository.currentLocation
    fun connect() {
//        wasalniSocket.initSocket()
    }
}
