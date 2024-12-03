package ua.kuk.data.services.playback

import android.app.PendingIntent
import android.content.Intent
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import androidx.media3.session.MediaSession
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

internal class BookPlaybackService : MediaLibraryService(), MediaLibrarySession.Callback {

    private var mediaLibrarySession: MediaLibrarySession? = null
    private var player: ExoPlayer? = null

    private val singleTaskActivityIntent: Intent by inject<Intent>(named(ua.kuk.common.DiQualifiers.MAIN_ACTIVITY))

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return mediaLibrarySession
    }

    override fun onCreate() {
        super.onCreate()
        initPlayer()
        initMediaSession()

    }

    private fun initPlayer() {
        val audioAttributes = androidx.media3.common.AudioAttributes.Builder()
            .setUsage(androidx.media3.common.C.USAGE_MEDIA)
            .setContentType(androidx.media3.common.C.AUDIO_CONTENT_TYPE_MOVIE)
            .build()

        player = ExoPlayer.Builder(this)
            .setAudioAttributes(audioAttributes, true)
            .build()
    }

    private fun initMediaSession() {
        mediaLibrarySession = player?.let {
            MediaLibrarySession.Builder(this, it, this)
                .setSessionActivity(getActivityPendingIntent())
                .build()
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        if (player?.playWhenReady == true) {
            player?.pause()
        }
        stopSelf()
    }

    override fun onDestroy() {
        mediaLibrarySession?.run {
            player.release()
            release()
            mediaLibrarySession = null
        }
        super.onDestroy()
    }

    private fun getActivityPendingIntent(): PendingIntent {
        return PendingIntent.getActivity(
            this,
            0,
            singleTaskActivityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}
