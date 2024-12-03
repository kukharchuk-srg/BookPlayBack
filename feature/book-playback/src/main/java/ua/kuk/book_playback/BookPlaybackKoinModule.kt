package ua.kuk.book_playback

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val bookPlaybackKoinModule
    get() = module {
        viewModel<PlaybackViewModel> { parameters ->
            PlaybackViewModel(parameters.get(), get(), get(), get())
        }
    }