package com.starbugs.wasalni_driver.service

import com.starbugs.wasalni_driver.R
import com.starbugs.wasalni_driver.ui.home.HomeActivity

import com.starbugs.wasalni_core.service.BaseTrackingService



class DriverTrackingService : BaseTrackingService() {

    override val binder: BaseTrackingService.LocalBinder = LocalBinder()
    override val notificationActivity: Class<*>
        get() = HomeActivity::class.java
    override val notificationTitle: Int
        get() = R.string.app_name
    override val notificationBody: Int
        get() = R.string.tracking_your_location
    override val notificationIcon: Int
        get() =  R.drawable.splash_icon

    inner class LocalBinder : BaseTrackingService.LocalBinder() {
        override val service: BaseTrackingService
            get() = this@DriverTrackingService
    }

}
