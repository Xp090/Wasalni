package com.starbugs.wasalni_core.util.bindingadpater

import android.view.View
import android.widget.Button
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.starbugs.wasalni_core.R
import com.starbugs.wasalni_core.data.holder.TripStateHolder
import com.starbugs.wasalni_core.data.holder.TripStateLiveData


@BindingAdapter(value = ["app:tripState", "app:showWhenTrip"], requireAll = true)
fun View.showWhenTrip(tripState: TripStateLiveData, conditions: Array<out TripStateHolder>) {
    visibility = if (conditions.contains(tripState.value)) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter(value = ["app:tripState", "app:goneWhenTrip"], requireAll = true)
fun View.goneWhenTrip(tripState: TripStateLiveData, conditions: Array<out TripStateHolder>) {
    visibility = if (conditions.contains(tripState.value)) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter(value = ["app:tripState", "app:visibleWhenTrip"], requireAll = true)
fun View.visibleWhenTrip(tripState: TripStateLiveData, conditions: Array<out TripStateHolder>) {
    visibility = if (conditions.contains(tripState.value)) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

@BindingAdapter(value = ["app:tripState", "app:inVisibleWhenTrip"], requireAll = true)
fun View.inVisibleWhenTrip(tripState: TripStateLiveData, conditions: Array<out TripStateHolder>) {
    visibility = if (conditions.contains(tripState.value)) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("app:tripActionButtonText")
fun Button.tripActionButtonText(tripState: TripStateLiveData) {
    @StringRes
    val textRes: Int = when (tripState.value) {
        TripStateHolder.Init -> R.string.begin
        TripStateHolder.SelectDestination -> R.string.confirm_destination
        TripStateHolder.SelectPickUp -> R.string.confirm_pickup
        TripStateHolder.ShowCost -> R.string.find_driver
        TripStateHolder.FindDriver -> R.string.cancel
        TripStateHolder.TripStarted -> R.string.trip_started
        null -> R.string.trip_started
    }

    setText(textRes)
}