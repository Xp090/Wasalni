package com.starbugs.wasalni_driver.di

import android.location.Geocoder
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.starbugs.wasalni_core.BuildConfig
import com.starbugs.wasalni_core.data.repository.CredentialsRepository
import com.starbugs.wasalni_core.data.repository.TripRepository
import com.starbugs.wasalni_core.data.repository.UserRepository
import com.starbugs.wasalni_core.data.source.SharedPreferenceSource
import com.starbugs.wasalni_core.data.source.SocketConnection
import com.starbugs.wasalni_core.data.source.TripApi
import com.starbugs.wasalni_core.data.source.WasalniUserApi
import com.starbugs.wasalni_core.util.WasalniInterceptor
import com.starbugs.wasalni_driver.data.repository.DriverTripRepository
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

    single { DriverTripRepository(get(), get(), get(), get()) }
}
