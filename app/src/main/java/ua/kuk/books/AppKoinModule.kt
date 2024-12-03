package ua.kuk.books

import android.content.Intent
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ua.kuk.common.DiQualifiers

val appKoinModule
    get() = module {
        single<Intent>(named(DiQualifiers.MAIN_ACTIVITY)) {
            Intent(
                androidContext(),
                MainActivity::class.java
            )
        }
    }