package com.starbugs.wasalni_driver

import com.starbugs.wasalni_core.BaseApp
import com.starbugs.wasalni_driver.di.repositoryModule
import com.starbugs.wasalni_driver.di.viewModelModule
import org.koin.core.module.Module

class DriverApp : BaseApp() {

    override fun getKoinModules(): List<Module> = listOf(viewModelModule,repositoryModule)

}