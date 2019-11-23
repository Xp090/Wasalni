package com.starbugs.wasalni_driver.ui.home

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import com.starbugs.wasalni_core.ui.BaseActivity
import com.starbugs.wasalni_driver.R
import com.starbugs.wasalni_driver.databinding.ActivityHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.starbugs.wasalni_core.util.PermissionsHelper
import com.starbugs.wasalni_driver.service.DriverTrackingService


class HomeActivity : BaseActivity<ActivityHomeBinding>(), OnMapReadyCallback {

    override val mViewModel: HomeViewModel by viewModel()
    override fun getLayoutRes(): Int = R.layout.activity_home


    private lateinit var trackingService: DriverTrackingService
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.connect()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        PermissionsHelper.checkForRequiredPermissions(this ,object : BaseMultiplePermissionsListener() {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.let {
                    if (!it.areAllPermissionsGranted()) {
                        finish()
                        finishAffinity()
                    }else{
                        val trackingServiceIntent = Intent(this@HomeActivity, DriverTrackingService::class.java)
                        bindService(trackingServiceIntent, wasalniServiceConnection, Context.BIND_AUTO_CREATE or Context.BIND_ABOVE_CLIENT)
                    }
                }
            }
        })
    }

    private val wasalniServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as DriverTrackingService.LocalBinder
            trackingService = binder.service as DriverTrackingService

        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap!!
    }
}
