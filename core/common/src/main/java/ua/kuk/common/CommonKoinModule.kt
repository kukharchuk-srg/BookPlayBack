package ua.kuk.common

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ua.kuk.common.logger.AndroidLogcatLogger
import ua.kuk.common.logger.Logger
import ua.kuk.common.monitors.ConnectivityManagerNetworkMonitor
import ua.kuk.common.monitors.NetworkMonitor

val commonKoinModule
    get() = module {
        single<Logger> { AndroidLogcatLogger() }
        single<AppDispatchers> { AppDispatchers() }
        single<NetworkMonitor> { ConnectivityManagerNetworkMonitor(androidContext()) }
    }