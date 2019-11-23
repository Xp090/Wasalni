package com.starbugs.wasalni_core.di

import android.location.Geocoder
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.starbugs.wasalni_core.BuildConfig
import com.starbugs.wasalni_core.data.repository.CredentialsRepository
import com.starbugs.wasalni_core.data.repository.TripRepository
import com.starbugs.wasalni_core.data.repository.UserRepository
import com.starbugs.wasalni_core.data.source.SharedPreferenceSource
import com.starbugs.wasalni_core.data.source.WasalniSocket
import com.starbugs.wasalni_core.data.source.WasalniUserApi
import com.starbugs.wasalni_core.util.WasalniInterceptor
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

    single(createdAtStart = true) { createOkHttpClient(get()) }
    single(createdAtStart = true) { createRetrofit(get()) }
    single(createdAtStart = true) { createWebService<WasalniUserApi>(get()) }
    single(createdAtStart = true) { WasalniInterceptor(get()) }
    single {Geocoder(androidContext())}
    single { Moshi.Builder().build()}
    single { WasalniSocket(get(),get()) }
    single { TripRepository(get(),get())}



    single { SharedPreferenceSource(androidContext()) }
    single { UserRepository(get(),get()) }
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
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())).build()
}
inline fun <reified T> createWebService(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}