package com.starbugs.wasalni_core.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.*
import androidx.core.app.NotificationCompat
import com.google.android.gms.common.api.GoogleApiClient
import org.koin.android.ext.android.inject
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.starbugs.wasalni_core.data.repository.TripRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber


abstract class BaseTrackingService<TR: TripRepository> : Service() {

    private val disposables = CompositeDisposable()


    protected abstract val tripRepository: TR

    protected abstract val binder :LocalBinder
    protected lateinit var mGoogleApiClient: GoogleApiClient
    protected lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    protected lateinit var locationCallback: LocationCallback

    protected abstract val notificationActivity: Class<*>
    protected abstract val notificationTitle:Int
    protected abstract val notificationBody:Int
    protected abstract val notificationIcon:Int



    override fun onBind(intent: Intent): IBinder {
        return binder
    }


    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        startForeground(FOREGROUND_SERVICE_ID, buildNotification())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.let { onLocationChange(it.lastLocation) }
            }
        }
        val request = LocationRequest()
        request.interval = UPDATE_INTERVAL.toLong()
        request.fastestInterval = FASTEST_INTERVAL.toLong()
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        request.smallestDisplacement = DISPLACEMENT.toFloat()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@BaseTrackingService)
        fusedLocationProviderClient.requestLocationUpdates(request,
            locationCallback,
            Looper.getMainLooper())

        tripRepository.initSocketConnection()
    }


    private fun onLocationChange(location: Location) {
        Timber.w(location.latitude.toString())
        Timber.w(location.longitude.toString())
        tripRepository.currentLocation.value = LatLng(location.latitude, location.longitude)

    }



    private fun buildNotification(): Notification? {

        val channelId = "tracking"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Location Tracking"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(channelId, name, importance)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        val pendingIntent: PendingIntent =
            Intent(this,notificationActivity).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(getText(notificationTitle))
            .setContentText(getText(notificationBody))
            .setSmallIcon(notificationIcon)
            .setContentIntent(pendingIntent)
            .build()

    }

    override fun onDestroy() {
        tripRepository.disconnectSocket()
        disposables.clear()
        super.onDestroy()
    }

    protected fun launch(job: () -> Disposable) {
        disposables.add(job())
    }

    abstract inner class LocalBinder : Binder() {
       abstract val service: BaseTrackingService<TR>
    }


    companion object {
        const val NOTIFICATION_ID = 1
        const val FOREGROUND_SERVICE_ID = 1
        const val UPDATE_INTERVAL = 5000
        const val FASTEST_INTERVAL = 3000
        const val DISPLACEMENT = 10
    }

}
