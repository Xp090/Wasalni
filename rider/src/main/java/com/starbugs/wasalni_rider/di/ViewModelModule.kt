package com.starbugs.wasalni_rider.di

import com.starbugs.wasalni_rider.ui.home.HomeViewModel
import com.starbugs.wasalni_rider.ui.login.LoginViewModel
import com.starbugs.wasalni_rider.ui.splash.RiderSplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { RiderSplashViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get())}
}