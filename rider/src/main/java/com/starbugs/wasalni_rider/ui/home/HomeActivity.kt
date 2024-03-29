package com.starbugs.wasalni_rider.ui.home

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
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
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.maps.android.PolyUtil
import com.google.maps.android.SphericalUtil
import com.starbugs.wasalni_core.data.holder.NetworkState
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

    private var destinationMarker: Marker? = null
    private var pickUpMarker: Marker? = null

    private var driverMarker: Marker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as RxMapsFragment
        mapFragment.getRxMapAsync(this)

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
            mViewModel.rideRequest.value?.destinationAddress = it.address!!
            mViewModel.rideRequest.value?.destinationPoint = it.latLng!!
            setDestinationMarker(it.latLng!!)
        }


        pickupPlaceSelect.setPlaceFields(
            listOf(
                Place.Field.ID, Place.Field.NAME,
                Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS, Place.Field.LAT_LNG
            )
        )
        pickupPlaceSelect.setCountry("EG")
        pickupPlaceSelect.setOnPlaceSelectedListener {
            mViewModel.rideRequest.value?.pickupAddress = it.address!!
            mViewModel.rideRequest.value?.pickupPoint = it.latLng!!
            setPickUpMarker(it.latLng!!)
        }

        binding.tripActionBtn.setOnClickListener {
            when(mViewModel.tripUiState.value){
                is TripStateHolder.Init -> {
                    rxGoogleMap.mapInstance.animateCamera(CameraUpdateFactory.newLatLngZoom(mViewModel.currentLocation.value!!,15.0f))
                }
                is TripStateHolder.SelectDestination -> {
                    setDestinationMarker(animate = false)
                    rxGoogleMap.mapInstance.animateCamera(CameraUpdateFactory.newLatLngZoom(mViewModel.currentLocation.value!!,15.0f))
                }
                is TripStateHolder.SelectPickUp -> {
                    setPickUpMarker(animate = false)
                    mViewModel.isLoading.value = true
                    mViewModel.getTripEstimatedInfo().observeOnce(this,
                        Observer {
                            when (it) {
                                is NetworkState.Success -> {
                                    val points = it.data.tripDirections.polylines
                                        .flatMap {poly -> PolyUtil.decode(poly) }
                                    val polylineOptions = PolylineOptions()
                                        .addAll(points)
                                        .width(10f)
                                        .color(Color.RED)
                                    rxGoogleMap.mapInstance.addPolyline(polylineOptions)
                                    mViewModel.isLoading.value = false
                                }
                            }
                        })
                }
                is TripStateHolder.ShowCost -> {
                    mViewModel.findDriver(mViewModel.rideRequest.value!!)
                    mViewModel.isLoading.value = true
                }
                is TripStateHolder.FindDriver -> {
                    mViewModel.tripUiState.previousState();
                    mViewModel.isLoading.value = false
                    return@setOnClickListener
                }
            }
            mViewModel.tripUiState.nextState()

        }

        mViewModel.currentTrip.observe(this, Observer { state ->

            state?.let {
                when (it) {
                    is NetworkState.Success -> {
                        if (mViewModel.tripUiState.value is TripStateHolder.FindDriver) {
                            mViewModel.tripUiState.nextState()
                            Toast.makeText(this,"Driver ${it.data.driver.email} accepted your request",Toast.LENGTH_LONG).show()
                            Timber.i("DRIVER: ${it.data.driver.email}")
                        } else if (mViewModel.tripUiState.value is TripStateHolder.Init) {
                            mViewModel.tripUiState.value = TripStateHolder.TripStarted
                        }
                    }
                    is NetworkState.Failure -> {
                        if (mViewModel.tripUiState.value is TripStateHolder.FindDriver) {
                            Toast.makeText(this,it.error.localizedMessage,Toast.LENGTH_LONG).show()
                            Timber.i("DRIVER: ${it.error.localizedMessage}")
                            rxGoogleMap.mapInstance.clear()
                        }
                        mViewModel.tripUiState.value = TripStateHolder.Init
                    }
                }
            }
        })

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
                            if (!::trackingService.isInitialized) {
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
                }
            })
    }



    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        val binder = service as RiderTrackingService.LocalBinder
        trackingService = binder.service as RiderTrackingService

        mViewModel.currentLocation.observeOnce(this, Observer {
            rxGoogleMap.mapInstance.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15.0f))
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
                mViewModel.rideRequest.value?.destinationPoint = it
                destinationPlaceSelect.setText("Dest Address")
                mViewModel.rideRequest.value?.destinationAddress = "Dest Address"
//                mViewModel.geocodeAddress(it).observeOnce(this, Observer {address ->
//                    destinationPlaceSelect.setText(address)
//                    mViewModel.rideRequest.value?.destinationAddress = address
//                })
            } else if (mViewModel.tripUiState.value is TripStateHolder.SelectPickUp) {
                mViewModel.rideRequest.value?.pickupPoint = it
                pickupPlaceSelect.setText("Pickup address")
                mViewModel.rideRequest.value?.pickupAddress = "Pickup address"
//                mViewModel.geocodeAddress(it).observeOnce(this, Observer {address ->
//                    pickupPlaceSelect.setText(address)
//                    mViewModel.rideRequest.value?.pickupAddress = address
//                })
            }

        }

        mViewModel.driverLocation.observe(this, Observer {
            when (it) {
                is NetworkState.Success -> {
                    if (driverMarker == null) {
                        driverMarker = rxGoogleMap.mapInstance.addMarker(MarkerOptions().position(it.data))
                    }else {
                        driverMarker!!.position = it.data
                    }

                }
            }
        })
    }

    private fun setDestinationMarker (lngLat: LatLng = rxGoogleMap.mapInstance.cameraPosition.target, animate: Boolean = true){
      destinationMarker =  rxGoogleMap.mapInstance.addMarker(MarkerOptions()
            .position(lngLat)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_marker_wasalni))
        )
      if (animate) rxGoogleMap.mapInstance.animateCamera(CameraUpdateFactory.newLatLngZoom(lngLat,15.0f))
    }

    private fun setPickUpMarker (lngLat: LatLng = rxGoogleMap.mapInstance.cameraPosition.target, animate: Boolean = true){
      pickUpMarker = rxGoogleMap.mapInstance.addMarker(MarkerOptions()
            .position(lngLat)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.wasalni_marker))
        )
        if (animate) rxGoogleMap.mapInstance.animateCamera(CameraUpdateFactory.newLatLngZoom(lngLat,15.0f))
    }

}
