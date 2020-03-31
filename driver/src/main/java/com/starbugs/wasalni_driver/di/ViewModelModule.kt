package com.starbugs.wasalni_driver.di

import com.starbugs.wasalni_driver.ui.incomingrequest.IncomingRequestViewModel
import com.starbugs.wasalni_driver.ui.home.HomeViewModel
import com.starbugs.wasalni_driver.ui.login.LoginViewModel
import com.starbugs.wasalni_driver.ui.splash.DriverSplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { DriverSplashViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get())}
    viewModel {
        IncomingRequestViewModel(
            get()
        )
    }
}