package com.starbugs.wasalni_rider.ui.home

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.starbugs.wasalni_core.ui.BaseActivity
import com.starbugs.wasalni_rider.R

import org.koin.androidx.viewmodel.ext.android.viewModel
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.starbugs.wasalni_core.util.PermissionsHelper
import com.starbugs.wasalni_rider.databinding.ActivityHomeBinding
import com.starbugs.wasalni_rider.service.RiderTrackingService
import kotlinx.android.synthetic.main.activity_home.*
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.maps.android.SphericalUtil
import com.starbugs.wasalni_core.data.holder.TripStateHolder
import com.starbugs.wasalni_core.util.ext.observeOnce
import com.starbugs.wasalni_core.util.ext.setOnPlaceSelectedListener
import com.starbugs.wasalni_core.util.view.OnRxMapReadyCallback
import com.starbugs.wasalni_core.util.view.RxGoogleMap
import com.starbugs.wasalni_core.util.view.RxMapsFragment
import timber.log.Timber


class HomeActivity : BaseActivity<ActivityHomeBinding>(), OnRxMapReadyCallback, ServiceConnection {

    override val mViewModel: HomeViewModel by viewModel()
    override fun getLayoutRes(): Int = R.layout.activity_home


    private lateinit var trackingService: RiderTrackingService
    private lateinit var rxGoogleMap: RxGoogleMap

    private lateinit var destinationPlaceSelect: AutocompleteSupportFragment
    private lateinit var pickupPlaceSelect: AutocompleteSupportFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as RxMapsFragment?
        mapFragment!!.getRxMapAsync(this)

        destinationPlaceSelect =
            supportFragmentManager.findFragmentById(R.id.destination_place_select) as AutocompleteSupportFragment
        pickupPlaceSelect =
            supportFragmentManager.findFragmentById(R.id.pickup_place_select) as AutocompleteSupportFragment
        destinationPlaceSelect.setPlaceFields(
            listOf(
                Place.Field.ID, Place.Field.NAME,
                Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS, Place.Field.LAT_LNG
            )
        )
        destinationPlaceSelect.setCountry("EG")

        destinationPlaceSelect.setOnPlaceSelectedListener {
            mViewModel.tripRequest.destinationAddress = it.address
            mViewModel.tripRequest.destinationPoint = it.latLng
        }


        pickupPlaceSelect.setPlaceFields(
            listOf(
                Place.Field.ID, Place.Field.NAME,
                Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS, Place.Field.LAT_LNG
            )
        )
        pickupPlaceSelect.setCountry("EG")
        pickupPlaceSelect.setOnPlaceSelectedListener {
            mViewModel.tripRequest.pickupAddress = it.address
            mViewModel.tripRequest.pickupPoint = it.latLng
        }

        binding.tripActionBtn.setOnClickListener {

        }
    }

    override fun onStart() {
        super.onStart()
        PermissionsHelper.checkForRequiredPermissions(this,
            object : BaseMultiplePermissionsListener() {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (!it.areAllPermissionsGranted()) {
                            finish()
                            finishAffinity()
                        } else {
                            val trackingServiceIntent =
                                Intent(this@HomeActivity, RiderTrackingService::class.java)
                            bindService(
                                trackingServiceIntent,
                                this@HomeActivity,
                                Context.BIND_AUTO_CREATE or Context.BIND_ABOVE_CLIENT
                            )
                        }
                    }
                }
            })
    }

    override fun onStop() {
        super.onStop()
        unbindService(this)
    }


    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        val binder = service as RiderTrackingService.LocalBinder
        trackingService = binder.service as RiderTrackingService

        mViewModel.currentLocation.observeOnce(this, Observer {
            rxGoogleMap.mapInstance.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 15.0f))
            val northeast = SphericalUtil.computeOffset(it, 10000.0, 45.0)
            val southwest = SphericalUtil.computeOffset(it, 10000.0, 225.0)
            destinationPlaceSelect.setLocationBias(RectangularBounds.newInstance(southwest, northeast))
            pickupPlaceSelect.setLocationBias(RectangularBounds.newInstance(southwest, northeast))
        })
    }

    override fun onServiceDisconnected(name: ComponentName) {

    }

    override fun onMapReady(rxMap: RxGoogleMap) {
        this.rxGoogleMap = rxMap
        rxGoogleMap.onCameraIdleListenerWithDebounce(500) {
            lat.text = it.latitude.toString()
            lng.text = it.longitude.toString()
            if (mViewModel.tripUiState.value is TripStateHolder.SelectDestination) {
                mViewModel.tripRequest.destinationPoint = it
                mViewModel.geocodeAddress(it).observeOnce(this, Observer {address ->
                    destinationPlaceSelect.setText(address)
                    mViewModel.tripRequest.destinationAddress = address
                })
            } else if (mViewModel.tripUiState.value is TripStateHolder.SelectPickUp) {
                mViewModel.tripRequest.pickupPoint = it
                mViewModel.geocodeAddress(it).observeOnce(this, Observer {address ->
                    pickupPlaceSelect.setText(address)
                    mViewModel.tripRequest.pickupAddress = address
                })
            }

        }
    }

}
