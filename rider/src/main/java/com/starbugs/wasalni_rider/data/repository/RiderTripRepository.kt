package com.starbugs.wasalni_driver.data.repository

import android.location.Geocoder
import com.starbugs.wasalni_core.data.repository.TripRepository
import com.starbugs.wasalni_core.data.repository.UserRepository
import com.starbugs.wasalni_core.data.source.SocketConnection
import com.starbugs.wasalni_core.data.source.TripApi


class RiderTripRepository (socketConnection: SocketConnection,
                           userRepository: UserRepository,
                           tripApi: TripApi,
                           geocoder: Geocoder):TripRepository(socketConnection,userRepository,tripApi,geocoder) {


}