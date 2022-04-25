package com.matrixdialogs.playbackservice

import android.app.PendingIntent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.matrixdialogs.core.DispatcherProvider
import com.matrixdialogs.playbackservice.callbacks.DialogEventListener
import com.matrixdialogs.playbackservice.callbacks.DialogNotificationListener
import com.matrixdialogs.playbackservice.callbacks.PlayBackPreparer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
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
    lateinit var dispatcherProvider: DispatcherProvider

    private val serviceScope = CoroutineScope(dispatcherProvider.main + serviceJob)

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    private lateinit var playNotificationManager: DialogNotificationManager
    var isForegroundService = false
    private var currentTrack : MediaMetadataCompat? = null

    override fun onCreate() {
        super.onCreate()
        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, 0)
        }

        serviceScope.launch {
            mediaSource.fetchData()
        }

        mediaSession = MediaSessionCompat(this, SERVICE_TAG).apply {
            setSessionActivity(activityIntent)
            isActive = true
        }
        sessionToken = mediaSession.sessionToken
        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlayer(exoPlayer)
        playNotificationManager = DialogNotificationManager(
            this,
            mediaSession.sessionToken,
            DialogNotificationListener(this),
        ) {
        }

        val playBackPreparer = PlayBackPreparer(mediaSource) {
            currentTrack = it
            preparePlayer(mediaSource.tracks, it, true)
        }
        mediaSessionConnector.setPlaybackPreparer(playBackPreparer)

        exoPlayer.addListener(DialogEventListener(this))
        playNotificationManager.showNotification(exoPlayer)
    }

    private fun preparePlayer(tracks: List<MediaMetadataCompat>, item: MediaMetadataCompat?, playNow: Boolean) {
        val currentTrackIndex = if (currentTrack == null) 0 else tracks.indexOf(item)
        // TODO check list
        with (exoPlayer) {
            setMediaSource(mediaSource.asMediaSource(dataSourceFactory))
            seekTo(currentTrackIndex, 0L)
            playWhenReady = playNow
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        TODO("Not yet implemented")
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        TODO("Not yet implemented")
    }
}