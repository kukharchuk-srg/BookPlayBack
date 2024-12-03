package ua.kuk.books

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ua.kuk.book_playback.bookPlaybackKoinModule
import ua.kuk.common.commonKoinModule
import ua.kuk.common.logger.Logger
import ua.kuk.data.dataKoinModule


class BookPlayBackApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@BookPlayBackApp)
            modules(appKoinModule, commonKoinModule, dataKoinModule, bookPlaybackKoinModule)
        }
        Logger.startTimber()
    }
}