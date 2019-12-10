package com.starbugs.wasalni_driver.service

import android.content.Intent
import com.starbugs.wasalni_driver.R
import com.starbugs.wasalni_driver.ui.home.HomeActivity

import com.starbugs.wasalni_core.service.BaseTrackingService
import com.starbugs.wasalni_core.util.ext.schedule
import com.starbugs.wasalni_driver.ui.IncomingRequestActivity
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.startActivity


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


    override fun onCreate() {
        super.onCreate()
        launch {
            this.wasalniSocket.listenToIncomingRequests()
                .schedule()
                .subscribe {
                    startActivity(intentFor<IncomingRequestActivity>().newTask())
                }
        }
    }
}
