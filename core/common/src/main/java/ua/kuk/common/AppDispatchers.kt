package ua.kuk.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
/**
 * Provides CoroutineDispatchers for different types of work.

 * Using this class makes it easier to provide custom dispatchers for testing.
 * You can replace the dispatchers with test-specific implementations (e.g., a TestDispatcher)
 * to control and verify coroutine execution in unit tests.
 */
class AppDispatchers(
    val default: CoroutineDispatcher = Dispatchers.Default,
    val io: CoroutineDispatcher = Dispatchers.IO,
    val main: MainCoroutineDispatcher = Dispatchers.Main,
    val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
)