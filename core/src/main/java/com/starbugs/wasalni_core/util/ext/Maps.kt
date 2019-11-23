package com.starbugs.wasalni_core.util.ext

import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


fun AutocompleteSupportFragment.setOnPlaceSelectedListener(onSuccess: (place: Place) -> Unit,
                                                           onError: ((status: Status) -> Unit)?) {

    setOnPlaceSelectedListener(object: PlaceSelectionListener{
        override fun onPlaceSelected(p0: Place) {
            onSuccess(p0)
        }

        override fun onError(p0: Status) {
            onError?.invoke(p0)
        }

    })

}

fun AutocompleteSupportFragment.setOnPlaceSelectedListener(onSuccess: (place: Place) -> Unit) {

    setOnPlaceSelectedListener(onSuccess,null)

}





