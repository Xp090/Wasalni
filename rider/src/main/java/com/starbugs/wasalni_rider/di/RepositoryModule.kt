package com.starbugs.wasalni_rider.di

import com.starbugs.wasalni_driver.data.repository.RiderTripRepository
import org.koin.dsl.module


val repositoryModule = module {

    single { RiderTripRepository(get(), get(), get(), get()) }
}
