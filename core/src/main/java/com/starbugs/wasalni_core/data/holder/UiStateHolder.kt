package com.starbugs.wasalni_core.data.holder

import androidx.lifecycle.MutableLiveData

sealed class TripStateHolder {

    object Init: TripStateHolder()
    object SelectDestination: TripStateHolder()
    object SelectPickUp: TripStateHolder()
    object ShowCost: TripStateHolder()
    object FindDriver: TripStateHolder()
    object TripStarted: TripStateHolder()

}



class TripStateLiveData : MutableLiveData<TripStateHolder>(TripStateHolder.Init){

    fun nextState(){
       value = when (value){
           is TripStateHolder.Init -> TripStateHolder.SelectDestination
           is TripStateHolder.SelectDestination -> TripStateHolder.SelectPickUp
           is TripStateHolder.SelectPickUp -> TripStateHolder.ShowCost
           is TripStateHolder.ShowCost -> TripStateHolder.FindDriver
           is TripStateHolder.FindDriver -> TripStateHolder.TripStarted
           is TripStateHolder.TripStarted -> TripStateHolder.TripStarted
           null -> TripStateHolder.Init
       }
    }

    fun preivousState(){
        value = when (value){
            is TripStateHolder.Init -> TripStateHolder.Init
            is TripStateHolder.SelectDestination -> TripStateHolder.Init
            is TripStateHolder.SelectPickUp -> TripStateHolder.SelectDestination
            is TripStateHolder.ShowCost -> TripStateHolder.SelectPickUp
            is TripStateHolder.FindDriver -> TripStateHolder.ShowCost
            is TripStateHolder.TripStarted -> TripStateHolder.TripStarted
            null -> TripStateHolder.Init
        }
    }
}

object TripStateUtil{

    @JvmStatic
    fun conditionsFrom(vararg states:TripStateHolder): Array<out TripStateHolder> {
        return states
    }
}