package com.starbugs.wasalni_core.data.repository

import androidx.lifecycle.MutableLiveData
import com.starbugs.wasalni_core.data.model.User
import com.starbugs.wasalni_core.data.source.WasalniUserApi
import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.data.holder.WasalniPersistenceError
import com.starbugs.wasalni_core.util.ext.mapToNetworkState
import io.reactivex.Single

class UserRepository (private val userApi: WasalniUserApi,
                      private val credentialsRepository: CredentialsRepository) {

    var userData: MutableLiveData<User?> = MutableLiveData()
        private set



    fun login(email: String, password: String): Single<NetworkState<User>>  {
        return userApi.login(email, password)
            .doOnSuccess {
                credentialsRepository.userToken = it.token
                userData.postValue(it.user)
            }.map { it.user }
            .mapToNetworkState()
    }

    fun fetchLoggedInUserData(): Single<NetworkState<User>> {
        return if (credentialsRepository.userToken != null) {
            userApi.getUser()
                .doOnSuccess { userData.postValue(it) }
                .mapToNetworkState()
        }else {
            Single.just(
                NetworkState.Failure(
                    WasalniPersistenceError.UserNotLoggedIn))
        }

    }
}