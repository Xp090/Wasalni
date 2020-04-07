package com.starbugs.wasalni_rider.service

import com.starbugs.wasalni_rider.R
import com.starbugs.wasalni_rider.ui.home.HomeActivity
import com.starbugs.wasalni_core.service.BaseTrackingService
import com.starbugs.wasalni_driver.data.repository.RiderTripRepository
import org.koin.android.ext.android.inject


class RiderTrackingService : BaseTrackingService<RiderTripRepository>() {

    override val tripRepository: RiderTripRepository by inject()

    override val binder: BaseTrackingService<RiderTripRepository>.LocalBinder = LocalBinder()
    override val notificationActivity: Class<*> = HomeActivity::class.java
    override val notificationTitle: Int = R.string.app_name
    override val notificationBody: Int = R.string.tracking_your_location
    override val notificationIcon: Int =  R.mipmap.wasalni_rider_icon







   inner class LocalBinder : BaseTrackingService<RiderTripRepository>.LocalBinder() {
        override val service: BaseTrackingService<RiderTripRepository>
            get() = this@RiderTrackingService
    }

}
