package com.starbugs.wasalni_core.data.holder

import com.starbugs.wasalni_core.util.annotation.ErrorStringId
import com.starbugs.wasalni_core.util.extra.ResponseErrorParser
import timber.log.Timber

sealed class NetworkState<T> {
    class Initial<T> : NetworkState<T>()
    class Loading<T> : NetworkState<T>()
    data class Failure<T>( val error: WasalniError) : NetworkState<T>()
    data class Success<T>(val data: T) : NetworkState<T>()

    companion object {
        fun <T> failureFromThrowable(throwable: Throwable): Failure<T> {
            return Failure(
                ResponseErrorParser.parse(throwable)
            )
        }
    }
}


sealed class WasalniError {
    val localizedMessage by lazy {
        val errorStringId = this::class.annotations
            .find { annotation -> annotation is ErrorStringId }
            ?.let { errorStringId -> (errorStringId as ErrorStringId).stringIdName}
        if (errorStringId != null) {
            return@lazy ResponseErrorParser().getLocalizedMessage(errorStringId)
        }else{
            Timber.w("Class: ${this::class.simpleName} has no ErrorStringId annotation ")
            return@lazy this::class.simpleName
        }
    }
    @ErrorStringId("unknown_error")
    object UnknownError : WasalniError()

}

sealed class WasalniHttpError : WasalniError() {

    @ErrorStringId("wrong_username_or_password")
    object WrongUserNameOrPassword : WasalniHttpError()

}

sealed class WasalniNetworkError : WasalniError() {
    @ErrorStringId("network_error")
    object NetworkError : WasalniNetworkError()
}

sealed class WasalniPersistenceError : WasalniError() {

    object UserNotLoggedIn : WasalniPersistenceError()
}