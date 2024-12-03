package ua.kuk.data.utils

import android.content.Context
import android.net.Uri
import ua.kuk.data.R
import ua.kuk.data.model.BookAudioInfo
import ua.kuk.data.model.KeyPoint

internal class MockDataProviderImpl(private val context: Context) : MockDataProvider {

    override fun getBookAudioInfo(): BookAudioInfo {
        return BookAudioInfo(
            id = 1,
            logo = getUriForRawRes("logo_173308550"),
            bookTitle = context.getString(R.string.mock_title_book_1),
            keyPoints = listOf(
                KeyPoint(
                    id = 173308550_1,
                    position = 0,
                    title = context.getString(R.string.mock_keypoint_1),
                    getUriForRawRes("keypoint_173308550_1")
                ),
                KeyPoint(
                    id = 173308550_2,
                    position = 1,
                    title = context.getString(R.string.mock_keypoint_2),
                    getUriForRawRes("keypoint_173308550_2")
                ),
                KeyPoint(
                    id = 173308550_3,
                    position = 2,
                    title = context.getString(R.string.mock_keypoint_3),
                    getUriForRawRes("keypoint_173308550_3")
                ),
                KeyPoint(
                    id = 173308550_4,
                    position = 3,
                    title = context.getString(R.string.mock_keypoint_4),
                    getUriForRawRes("keypoint_173308550_4")
                ),
                KeyPoint(
                    id = 173308550_5,
                    position = 4,
                    title = context.getString(R.string.mock_keypoint_5),
                    getUriForRawRes("keypoint_173308550_5")
                ),
            )
        )
    }

    private fun getUriForRawRes(rawResName: String): Uri {
        return Uri.parse("android.resource://${context.packageName}/raw/$rawResName")
    }
}