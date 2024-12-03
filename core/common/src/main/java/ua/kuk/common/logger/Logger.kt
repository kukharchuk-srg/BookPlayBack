package ua.kuk.common.logger

import timber.log.Timber
import ua.kuk.common.BuildConfig

interface Logger {
    fun d(tag: String, message: String)
    fun e(tag: String, message: String, error: Throwable? = null)

    companion object {
        fun startTimber() {
            if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree()) else Timber.plant(
                CrashReportingTree()
            )
        }
    }
}

fun AndroidLogcatLogger(): Logger = object : Logger {
    override fun d(tag: String, message: String) {
        Timber.tag(tag).d(message)
    }

    override fun e(tag: String, message: String, error: Throwable?) {
        Timber.tag(tag).e(error, message)
    }
}