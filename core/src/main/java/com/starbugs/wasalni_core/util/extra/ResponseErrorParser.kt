package com.starbugs.wasalni_core.util.extra

import android.content.Context
import com.squareup.moshi.Moshi
import com.starbugs.wasalni_core.R
import com.starbugs.wasalni_core.data.holder.ApplicationHttpError
import com.starbugs.wasalni_core.data.holder.ApplicationNetworkError
import com.starbugs.wasalni_core.data.holder.ApplicationError
import com.starbugs.wasalni_core.data.holder.ApplicationSocketError
import com.starbugs.wasalni_core.util.annotation.ErrorStringId
import com.starbugs.wasalni_core.util.exception.SocketErrorException
import io.reactivex.Notification
import org.json.JSONObject
import org.koin.core.KoinComponent
import org.koin.core.get
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import kotlin.reflect.KClass


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
        fun parse(throwable: Throwable): ApplicationError {
            return when (throwable) {
                is HttpException -> {
                    val errorResponse = throwable.response()?.errorBody()?.string()
                    val jsonObject = JSONObject(errorResponse!!)
                    val errorMessage = jsonObject.getString("message")
                    findErrorClass(ApplicationHttpError::class,errorMessage)
                }
                is SocketErrorException ->{
                    findErrorClass(ApplicationSocketError::class, throwable.errorStringId)
                }
                is IOException -> ApplicationNetworkError.NetworkError

                else ->  ApplicationError.UnknownError
            }

        }

          fun <T: ApplicationError> findErrorClass(errorTypeClass: KClass<T>, errorResponse: String?): ApplicationError {
            val errorClass = errorTypeClass.nestedClasses
                .find { klass ->
                    klass.annotations.any { annotation ->
                        when (annotation) {
                            is ErrorStringId -> annotation.stringIdName == errorResponse
                            else -> false
                        }
                    }
                }
            return ((errorClass?.objectInstance ?:  ApplicationError.UnknownError) as ApplicationError)
                .apply { errorResponse?.let { stringId = it } }
        }

    }
}