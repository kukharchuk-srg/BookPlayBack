package ua.kuk.data.model

import android.net.Uri

data class KeyPoint(
    val id: Int,
    val position: Int,
    val title: String,
    val audio: Uri
)
