package ua.kuk.data.utils

import ua.kuk.data.model.BookAudioInfo

/** This tool is made only for testing the functionality of the application as part of a test task */

interface MockDataProvider {
    fun getBookAudioInfo(): BookAudioInfo
}