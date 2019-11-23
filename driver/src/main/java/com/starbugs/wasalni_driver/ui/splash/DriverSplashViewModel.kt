package com.starbugs.wasalni_driver.ui.splash

import androidx.lifecycle.LiveData
import com.starbugs.wasalni_core.data.model.User
import com.starbugs.wasalni_core.data.repository.UserRepository
import com.starbugs.wasalni_core.ui.BaseViewModel
import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.util.ext.subscribeWithParsedError
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

class DriverSplashViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    fun fetchUserDataIfLoggedIn(splashScreenTimeout: Int): LiveData<NetworkState<User>> {
        return launchWithLiveData {liveData ->
            Single.zip(userRepository.fetchLoggedInUserData(),
                Single.timer(splashScreenTimeout.toLong(), TimeUnit.MILLISECONDS),
                BiFunction<NetworkState<User>, Long, NetworkState<User>> { user, _ -> user })
                .subscribeWithParsedError(liveData)
        }

    }

}
