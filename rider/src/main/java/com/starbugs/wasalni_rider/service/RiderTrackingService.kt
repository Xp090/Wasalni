package com.starbugs.wasalni_rider.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.*
import androidx.core.app.NotificationCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.starbugs.wasalni_core.data.source.WasalniSocket
import com.starbugs.wasalni_rider.R
import com.starbugs.wasalni_rider.ui.home.HomeActivity
import org.koin.android.ext.android.inject
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.starbugs.wasalni_core.data.model.TripRequest
import com.starbugs.wasalni_core.data.repository.UserRepository
import com.starbugs.wasalni_core.service.BaseTrackingService
import com.starbugs.wasalni_core.util.ext.schedule
import timber.log.Timber


class RiderTrackingService : BaseTrackingService() {
    override val binder: BaseTrackingService.LocalBinder = LocalBinder()
    override val notificationActivity: Class<*>
        get() = HomeActivity::class.java
    override val notificationTitle: Int
        get() = R.string.app_name
    override val notificationBody: Int
        get() = R.string.tracking_your_location
    override val notificationIcon: Int
        get() =  R.mipmap.wasalni_rider_icon

    fun findDriver(tripRequest: TripRequest) {
       launch {
           wasalniSocket.findDriver(tripRequest)
               .schedule()
               .subscribe({

               },{

               })
       }
    }

   inner class LocalBinder : BaseTrackingService.LocalBinder() {
        override val service: BaseTrackingService
            get() = this@RiderTrackingService
    }
}
