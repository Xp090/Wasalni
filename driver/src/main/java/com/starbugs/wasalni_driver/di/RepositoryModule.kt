package com.starbugs.wasalni_driver.di

import com.starbugs.wasalni_driver.data.repository.DriverTripRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { DriverTripRepository(get(), get(), get(), get()) }
}
