package com.starbugs.wasalni_rider

import com.starbugs.wasalni_core.BaseApp
import com.starbugs.wasalni_rider.di.repositoryModule
import com.starbugs.wasalni_rider.di.viewModelModule
import org.koin.core.module.Module

class RiderApp : BaseApp() {

    override fun getKoinModules(): List<Module> = listOf(viewModelModule,repositoryModule)

}