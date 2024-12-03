package ua.kuk.data

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ua.kuk.data.repository.BookPlayBackRepository
import ua.kuk.data.repository.BookPlayBackRepositoryImpl
import ua.kuk.data.services.playback.BookPlaybackManager
import ua.kuk.data.utils.MockDataProvider
import ua.kuk.data.utils.MockDataProviderImpl

val dataKoinModule
    get() = module {
        single<BookPlayBackRepository> {
            BookPlayBackRepositoryImpl(get(),get())
        }

        single<BookPlaybackManager> {
            BookPlaybackManager(androidContext(),get())
        }

        single<MockDataProvider> {
            MockDataProviderImpl(androidContext())
        }
    }