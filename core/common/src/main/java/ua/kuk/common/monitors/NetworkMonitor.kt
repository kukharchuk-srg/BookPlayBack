package ua.kuk.common.monitors

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    val isConnected: Flow<Boolean>
}