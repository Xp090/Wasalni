package com.starbugs.wasalni_driver.ui.home

import com.starbugs.wasalni_core.data.repository.TripRepository
import com.starbugs.wasalni_core.ui.BaseViewModel
import com.starbugs.wasalni_driver.data.repository.DriverTripRepository


class HomeViewModel(private val tripRepository: DriverTripRepository) : BaseViewModel() {
    val currentLocation = tripRepository.currentLocation
}
