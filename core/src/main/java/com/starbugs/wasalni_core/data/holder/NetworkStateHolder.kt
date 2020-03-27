package com.starbugs.wasalni_core.data.holder

import com.starbugs.wasalni_core.util.annotation.ErrorStringId
import com.starbugs.wasalni_core.util.extra.ResponseErrorParser
import timber.log.Timber

sealed class NetworkState<T> {

    fun success(): T? {
        return when (this) {
            is Success -> data
            else -> null
        }
    }
    fun failure(): ApplicationError? {
        return when (this) {
            is Failure -> error
            else -> null
        }
    }

    fun loading(): Boolean {
        return when (this) {
            is Loading -> true
            else -> false
        }
    }

    class Initial<T> : NetworkState<T>()
    class Loading<T> : NetworkState<T>()
    data class Failure<T>(val error: ApplicationError) : NetworkState<T>()
    data class Success<T>(val data: T) : NetworkState<T>()

    companion object {
        fun <T> failureFromThrowable(throwable: Throwable): Failure<T> {
            return Failure(
                ResponseErrorParser.parse(throwable)
            )
        }
    }
}


sealed class ApplicationError {
    val localizedMessage by lazy {
        ResponseErrorParser().getLocalizedMessage(stringId)
    }

    var stringId: String = "unknown_error"

    @ErrorStringId("unknown_error")
    object UnknownError : ApplicationError()

}

sealed class ApplicationHttpError : ApplicationError() {

    @ErrorStringId("wrong_username_or_password")
    object WrongUserNameOrPassword : ApplicationHttpError()

}

sealed class ApplicationSocketError : ApplicationError() {

    @ErrorStringId("no_driver_found")
    object NoDriverFound : ApplicationSocketError()

}

sealed class ApplicationNetworkError : ApplicationError() {
    @ErrorStringId("network_error")
    object NetworkError : ApplicationNetworkError()
}

sealed class ApplicationPersistenceError : ApplicationError() {

    object UserNotLoggedIn : ApplicationPersistenceError()
}