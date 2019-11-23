package com.starbugs.wasalni_core

import android.app.Application
import com.starbugs.wasalni_core.di.repositoryModule
import com.starbugs.wasalni_core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import timber.log.Timber
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import android.R.attr.apiKey
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



abstract class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidLogger()
            androidContext(this@BaseApp)
            modules(mutableListOf(repositoryModule, viewModelModule).apply { addAll(getKoinModules()) })
        }

        Places.initialize(applicationContext, getString(R.string.google_maps_api_key))
    }

    protected abstract fun getKoinModules():List<Module>
}