package com.starbugs.wasalni_core.di

import android.location.Geocoder
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.starbugs.wasalni_core.BuildConfig
import com.starbugs.wasalni_core.data.repository.CredentialsRepository
import com.starbugs.wasalni_core.data.repository.UserRepository
import com.starbugs.wasalni_core.data.source.SharedPreferenceSource
import com.starbugs.wasalni_core.data.source.SocketConnection
import com.starbugs.wasalni_core.data.source.TripApi
import com.starbugs.wasalni_core.data.source.UserApi
import com.starbugs.wasalni_core.util.WasalniInterceptor
import com.starbugs.wasalni_core.util.retrofit.MoshiConverterFactoryForNetworkState
import com.starbugs.wasalni_core.util.retrofit.RxCallFactoryForNetworkState
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

val repositoryModule = module {

    single { createOkHttpClient(get()) }
    single { createRetrofit(get()) }
    single { createWebService<UserApi>(get()) }
    single { createWebService<TripApi>(get()) }

    single { WasalniInterceptor(get()) }
    single { Geocoder(androidContext()) }
    single { Moshi.Builder().build() }
    single { SocketConnection(get(), get()) }


    single { SharedPreferenceSource(androidContext()) }
    single { UserRepository(get(), get()) }
    single { CredentialsRepository(get()) }


}

fun createOkHttpClient(wasalniInterceptor: WasalniInterceptor): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
        .connectTimeout(30L, TimeUnit.SECONDS)
        .readTimeout(30L, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(wasalniInterceptor)
        .build()
}

fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
    val moshi = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .build()
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactoryForNetworkState(moshi))
        .addCallAdapterFactory(RxCallFactoryForNetworkState())
        .build()
}

inline fun <reified T> createWebService(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}