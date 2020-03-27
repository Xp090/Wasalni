package com.starbugs.wasalni_driver.data.repository

import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.data.model.TripEstimiatedInfo
import com.starbugs.wasalni_core.data.model.TripRequest
import com.starbugs.wasalni_core.data.repository.TripRepository
import com.starbugs.wasalni_core.data.repository.UserRepository
import com.starbugs.wasalni_core.data.source.SocketConnection
import com.starbugs.wasalni_core.data.source.TripApi
import com.starbugs.wasalni_core.util.GeoUtils
import com.starbugs.wasalni_core.util.ext.mapToNetworkState
import io.reactivex.Single


class DriverTripRepository (socketConnection: SocketConnection,
                            userRepository: UserRepository,
                             tripApi: TripApi,
                             geocoder: Geocoder):TripRepository(socketConnection,userRepository,tripApi,geocoder) {


}