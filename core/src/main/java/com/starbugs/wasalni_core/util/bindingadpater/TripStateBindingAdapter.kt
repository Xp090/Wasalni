package com.starbugs.wasalni_core.util.bindingadpater

import android.view.View
import androidx.databinding.BindingAdapter
import com.starbugs.wasalni_core.data.holder.TripStateHolder
import com.starbugs.wasalni_core.data.holder.TripStateLiveData


@BindingAdapter(value = ["app:tripState", "app:showWhenTrip"], requireAll = true)
fun View.showWhenTrip(tripState: TripStateLiveData, conditions: Array<out TripStateHolder> ) {
    visibility = if (conditions.contains(tripState.value)){
        View.VISIBLE
    }else{
        View.GONE
    }
}

@BindingAdapter(value = ["app:tripState", "app:goneWhenTrip"], requireAll = true)
fun View.goneWhenTrip(tripState: TripStateLiveData, conditions: Array<out TripStateHolder> ) {
    visibility = if (conditions.contains(tripState.value)){
        View.GONE
    }else{
        View.VISIBLE
    }
}

@BindingAdapter(value = ["app:tripState", "app:visibleWhenTrip"], requireAll = true)
fun View.visibleWhenTrip(tripState: TripStateLiveData, conditions: Array<out TripStateHolder> ) {
    visibility = if (conditions.contains(tripState.value)){
        View.VISIBLE
    }else{
        View.INVISIBLE
    }
}

@BindingAdapter(value = ["app:tripState", "app:inVisibleWhenTrip"], requireAll = true)
fun View.inVisibleWhenTrip(tripState: TripStateLiveData, conditions: Array<out TripStateHolder> ) {
    visibility = if (conditions.contains(tripState.value)){
        View.INVISIBLE
    }else{
        View.VISIBLE
    }
}