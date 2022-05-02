package com.matrixdialogs.playbackservice

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.matrixdialogs.core.DispatcherProvider
import com.matrixdialogs.core.MEDIA_ROOT_ID
import com.matrixdialogs.core.di.IoDispatcher
import com.matrixdialogs.core.di.MainDispatcher
import com.matrixdialogs.playbackservice.callbacks.DialogEventListener
import com.matrixdialogs.playbackservice.callbacks.DialogNotificationListener
import com.matrixdialogs.playbackservice.callbacks.PlayBackPreparer
import com.matrixdialogs.playbackservice.service.DialogNotificationManager
import com.matrixdialogs.playbackservice.service.MediaSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

const val SERVICE_TAG = "MatrixDialogsPlayer"

@AndroidEntryPoint
class PlayBackService : MediaBrowserServiceCompat() {
    @Inject
    lateinit var dataSourceFactory: DefaultDataSource.Factory
    @Inject
    lateinit var exoPlayer: ExoPlayer
    @Inject
    lateinit var mediaSource: MediaSource

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    private lateinit var playNotificationManager: DialogNotificationManager
    var isForegroundService = false
    private var currentTrack : MediaMetadataCompat? = null
    private var isPlayerInitialized = false
    private lateinit var dialogListener : DialogEventListener

    private inner class PlayQueueNavigator : TimelineQueueNavigator(mediaSession) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat =
            mediaSource.tracks[windowIndex].description
    }

    companion object {
        var currentDialogDuration = 0L
            private set
    }

    override fun onCreate() {
        super.onCreate()
        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, 0)
        }

        mediaSession = MediaSessionCompat(this, SERVICE_TAG).apply {
            setSessionActivity(activityIntent)
            isActive = true
        }
        sessionToken = mediaSession.sessionToken

        playNotificationManager = DialogNotificationManager(
            this,
            mediaSession.sessionToken,
            DialogNotificationListener(this),
        ) {
            currentDialogDuration = exoPlayer.duration
        }

        val playBackPreparer = PlayBackPreparer(mediaSource) {
            currentTrack = it
            preparePlayer(mediaSource.tracks, it, true)
        }

        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlaybackPreparer(playBackPreparer)
        mediaSessionConnector.setQueueNavigator(PlayQueueNavigator())
        mediaSessionConnector.setPlayer(exoPlayer)

        dialogListener = DialogEventListener(this)
        exoPlayer.addListener(dialogListener)
        playNotificationManager.showNotification(exoPlayer)
    }

    private fun preparePlayer(tracks: List<MediaMetadataCompat>, item: MediaMetadataCompat?, playNow: Boolean) {
        val currentTrackIndex = if (currentTrack == null) 0 else tracks.indexOf(item)
        // TODO check list
        with (exoPlayer) {
            setMediaSource(mediaSource.asMediaSource(dataSourceFactory))
            seekTo(currentTrackIndex, 0L)
            playWhenReady = playNow
            prepare()
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        exoPlayer.removeListener(dialogListener)
        exoPlayer.release()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot = BrowserRoot(MEDIA_ROOT_ID, null)

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        when (parentId) {
            MEDIA_ROOT_ID -> {
                result.sendResult(mediaSource.asMediaItems())
                if (!isPlayerInitialized && mediaSource.tracks.isNotEmpty()) {
                    preparePlayer(mediaSource.tracks, mediaSource.tracks[0], false)
                    isPlayerInitialized = true
                }
            }
            else -> result.sendResult(null)
        }
    }
}