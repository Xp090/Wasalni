package com.starbugs.wasalni_rider.ui.login

import androidx.lifecycle.MutableLiveData
import com.starbugs.wasalni_core.data.model.User
import com.starbugs.wasalni_core.data.repository.UserRepository
import com.starbugs.wasalni_core.ui.BaseViewModel
import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.util.ext.subscribeWithParsedError


class LoginViewModel (private val userRepository: UserRepository) : BaseViewModel() {
    var username = MutableLiveData<String>().apply { value = "" }
    var password = MutableLiveData<String>().apply { value = "" }

    var userData = MutableLiveData<NetworkState<User>>()

    fun login() {
        userData.value = NetworkState.Loading()
        userRepository.login(username.value!!,password.value!!)
            .subscribeWithParsedError(userData)
    }
}
