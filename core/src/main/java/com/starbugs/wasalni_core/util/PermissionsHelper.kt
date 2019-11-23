package com.starbugs.wasalni_core.util

import android.Manifest
import android.app.Activity
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.BasePermissionListener
import com.karumi.dexter.listener.single.CompositePermissionListener
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.starbugs.wasalni_core.R


object PermissionsHelper {

    fun checkForRequiredPermissions(activity: Activity, listener: MultiplePermissionsListener?) {

        val dialogPermissionListener = DialogOnAnyDeniedMultiplePermissionsListener.Builder
            .withContext(activity)
            .withTitle(R.string.permsissons_requreid)
            .withMessage(R.string.you_cant_use_app)
            .withButtonText(android.R.string.ok)
            .withIcon(android.R.drawable.ic_dialog_alert)
            .build()

        val compositeListener =
            if (listener != null) {
                CompositeMultiplePermissionsListener(dialogPermissionListener, listener)
            }
            else {
                CompositeMultiplePermissionsListener(dialogPermissionListener)
            }

        Dexter.withActivity(activity)
            .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(compositeListener)
            .check()
    }
}