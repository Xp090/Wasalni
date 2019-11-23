package com.starbugs.wasalni_core.util

import com.starbugs.wasalni_core.data.repository.CredentialsRepository
import okhttp3.Interceptor
import okhttp3.Response


class WasalniInterceptor (private val credentialsRepository: CredentialsRepository): Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        credentialsRepository.userToken?.let { token ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization","Bearer $token")
                .addHeader("x-auth-token", token)
                .build()
            return chain.proceed(newRequest)
        }

        return chain.proceed(chain.request())
    }
}