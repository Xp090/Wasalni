package com.starbugs.wasalni_core.data.repository

import androidx.lifecycle.MutableLiveData
import com.starbugs.wasalni_core.data.model.User
import com.starbugs.wasalni_core.data.source.UserApi
import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.data.holder.ApplicationPersistenceError
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class UserRepository(
    private val userApi: UserApi,
    private val credentialsRepository: CredentialsRepository
) {


    val userData: BehaviorSubject<User> = BehaviorSubject.create()

    fun login(email: String, password: String): Single<NetworkState<User>> {
        return userApi.login(email, password)
            .doOnSuccess { resp ->
                credentialsRepository.userToken = resp.success()?.token
                resp.success()?.user?.also { userData.onNext(it) }
            }.map {
                  when (it) {
                    is NetworkState.Success -> NetworkState.Success(it.success()!!.user)
                    else -> NetworkState.Failure<User>(it.failure()!!)
                }

            }
    }

    fun fetchLoggedInUserData(): Single<NetworkState<User>> {
        return if (credentialsRepository.userToken != null) {
            userApi.getUserData()
                .doOnSuccess { resp -> resp.success()?.also { userData.onNext(it) }  }

        } else {
            Single.just(
                NetworkState.Failure(
                    ApplicationPersistenceError.UserNotLoggedIn
                )
            )
        }

    }
}