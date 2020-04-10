package com.starbugs.wasalni_driver.service

import com.starbugs.wasalni_driver.R
import com.starbugs.wasalni_driver.ui.home.HomeActivity

import com.starbugs.wasalni_core.service.BaseTrackingService
import com.starbugs.wasalni_core.util.ext.schedule
import com.starbugs.wasalni_driver.data.repository.DriverTripRepository
import com.starbugs.wasalni_driver.ui.incomingrequest.IncomingRequestActivity
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.koin.android.ext.android.inject


class DriverTrackingService : BaseTrackingService<DriverTripRepository>() {
    override val tripRepository: DriverTripRepository by inject()
    override val binder: BaseTrackingService<DriverTripRepository>.LocalBinder = LocalBinder()

    override val notificationActivity: Class<*> = HomeActivity::class.java
    override val notificationTitle: Int = R.string.app_name
    override val notificationBody: Int = R.string.tracking_your_location
    override val notificationIcon: Int = R.drawable.splash_icon




    override fun onCreate() {
        super.onCreate()

    }

    override fun onSocketConnected() {
        super.onSocketConnected()
        launch {
            tripRepository.listenForRiderRequest()
                .schedule()
                .subscribe {
                    startActivity(intentFor<IncomingRequestActivity>().newTask())
                }

        }
    }



    inner class LocalBinder : BaseTrackingService<DriverTripRepository>.LocalBinder() {
        override val service: BaseTrackingService<DriverTripRepository>
            get() = this@DriverTrackingService
    }
}
