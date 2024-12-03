package ua.kuk.data.model

import android.net.Uri

data class BookAudioInfo(
    val id: Int,
    val logo: Uri,
    val bookTitle: String,
    val keyPoints: List<KeyPoint>
)
