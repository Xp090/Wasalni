package com.starbugs.wasalni_core.util.extra

import android.content.Context
import com.starbugs.wasalni_core.R
import com.starbugs.wasalni_core.data.holder.WasalniHttpError
import com.starbugs.wasalni_core.data.holder.WasalniNetworkError
import com.starbugs.wasalni_core.data.holder.WasalniError
import com.starbugs.wasalni_core.util.annotation.ErrorStringId
import com.starbugs.wasalni_core.util.exception.SocketErrorExcpetion
import org.koin.core.KoinComponent
import org.koin.core.get
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException


class ResponseErrorParser : KoinComponent {
    private val context: Context = get()

    fun getLocalizedMessage(errorIdentifier: String): String? {
        val stringResId = context.resources!!.getIdentifier(errorIdentifier, "string", context.packageName)
        if (stringResId != 0) {
            return context.resources!!.getString(stringResId)
        }
        Timber.w("Error: $errorIdentifier is not declared in string.xml")
        return context.resources!!.getString(R.string.unknown_error)
    }

    companion object {
        fun parse(throwable: Throwable): WasalniError {
            return when (throwable) {
                is HttpException -> {
                    val errorResponse = throwable.response()?.errorBody()?.string()
                    getNetworkError(errorResponse)
                }
                is SocketErrorExcpetion ->{
                    getNetworkError(throwable.errorStringId)
                }
                is IOException -> WasalniNetworkError.NetworkError

                else ->  WasalniError.UnknownError
            }

        }

         private fun getNetworkError(errorResponse: String?): WasalniError {
            val errorClass = WasalniHttpError::class.nestedClasses
                .find { klass ->
                    klass.annotations.any { annotation ->
                        when (annotation) {
                            is ErrorStringId -> annotation.stringIdName == errorResponse
                            else -> false
                        }
                    }
                }
            errorClass?.let { return (it.objectInstance as WasalniError?)!! }
            return WasalniError.UnknownError
        }

    }
}