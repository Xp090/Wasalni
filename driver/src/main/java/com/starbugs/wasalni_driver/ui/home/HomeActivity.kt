package com.starbugs.wasalni_driver.ui.home

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.starbugs.wasalni_core.ui.BaseActivity
import com.starbugs.wasalni_driver.R
import com.starbugs.wasalni_driver.databinding.ActivityHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.maps.android.SphericalUtil
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.starbugs.wasalni_core.util.PermissionsHelper
import com.starbugs.wasalni_core.util.ext.observeOnce
import com.starbugs.wasalni_core.util.view.OnRxMapReadyCallback
import com.starbugs.wasalni_core.util.view.RxGoogleMap
import com.starbugs.wasalni_core.util.view.RxMapsFragment
import com.starbugs.wasalni_driver.service.DriverTrackingService


class HomeActivity : BaseActivity<ActivityHomeBinding>(), OnRxMapReadyCallback {

    override val mViewModel: HomeViewModel by viewModel()
    override fun getLayoutRes(): Int = R.layout.activity_home


    private lateinit var trackingService: DriverTrackingService
    private lateinit var rxGoogleMap: RxGoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.connect()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as RxMapsFragment?
        mapFragment!!.getRxMapAsync(this)

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
        if (intent != null) {
            onNewIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
       intent!!.getStringExtra("response")?.let {
           Toast.makeText(this,"Request Has been $it",Toast.LENGTH_LONG).show()
       }

    }

    private val wasalniServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as DriverTrackingService.LocalBinder
            trackingService = binder.service as DriverTrackingService

            mViewModel.currentLocation.observeOnce(this@HomeActivity, Observer {
                rxGoogleMap.mapInstance.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15.0f))
                rxGoogleMap.mapInstance.addMarker(
                    MarkerOptions()
                        .position(it)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.wasalni_marker)))
            })

        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    override fun onMapReady(rxMap: RxGoogleMap) {
        this.rxGoogleMap = rxMap
    }
}
