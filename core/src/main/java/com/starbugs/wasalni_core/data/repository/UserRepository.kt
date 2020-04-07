package com.starbugs.wasalni_core.data.repository

import androidx.lifecycle.MutableLiveData
import com.starbugs.wasalni_core.data.model.User
import com.starbugs.wasalni_core.data.source.UserApi
import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.data.holder.ApplicationPersistenceError
import com.starbugs.wasalni_core.util.ext.mapToNetworkState
import io.reactivex.Single

class UserRepository (private val userApi: UserApi,
                      private val credentialsRepository: CredentialsRepository) {

    var userData: MutableLiveData<User?> = MutableLiveData()
        private set



    fun login(email: String, password: String): Single<NetworkState<User>>  {
        return userApi.login(email, password)
            .doOnSuccess {
                credentialsRepository.userToken = it.success()?.token
                userData.postValue(it.success()?.user)
            }.map {
                when (it) {
                    is NetworkState.Success -> NetworkState.Success(it.success()!!.user)
                    else -> NetworkState.Failure(it.failure()!!)
                }
                
            }
    }

    fun fetchLoggedInUserData(): Single<NetworkState<User>> {
        return if (credentialsRepository.userToken != null) {
            userApi.getUser()
                .doOnSuccess { userData.postValue(it.success()) }
        }else {
            Single.just(
                NetworkState.Failure(
                    ApplicationPersistenceError.UserNotLoggedIn))
        }

    }
}