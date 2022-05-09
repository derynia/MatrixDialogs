package com.matrixdialogs.playbackservice.service

import android.content.ComponentName
import android.content.Context
import android.media.browse.MediaBrowser
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.matrixdialogs.core.Event
import com.matrixdialogs.core.MEDIA_REPEATS_KEY
import com.matrixdialogs.core.Resource
import com.matrixdialogs.playbackservice.PlayBackService
import com.matrixdialogs.playbackservice.R

class PlayServiceConnection(
    context: Context
) {
    private val _isConnected = MutableLiveData<Event<Resource<Boolean>>>()
    val isConnected : LiveData<Event<Resource<Boolean>>> = _isConnected

    private val _playbackState = MutableLiveData<PlaybackStateCompat?>().apply {
        postValue(EMPTY_PLAYBACK_STATE)
    }
    val playbackState : LiveData<PlaybackStateCompat?> = _playbackState

    private val _currentlyPlaying = MutableLiveData<MediaMetadataCompat>()
    val currentlyPlaying : LiveData<MediaMetadataCompat> = _currentlyPlaying

    lateinit var mediaController: MediaControllerCompat
    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)
    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(
            context,
            PlayBackService::class.java
        ),
        mediaBrowserConnectionCallback,
        null
    ).apply {
        connect()
    }

    val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    private inner class MediaBrowserConnectionCallback(
        private val context: Context
    ) : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                registerCallback(MediaControllerCallback())
            }
            _isConnected.postValue(Event(Resource.success(true)))
        }

        override fun onConnectionSuspended() {
            _isConnected.postValue(Event(Resource.error(context.getString(R.string.suspended_connection), false)))
        }

        override fun onConnectionFailed() {
            _isConnected.postValue(Event(Resource.error(context.getString(R.string.couldnt_connect), false)))
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            //Log.d("PlayService", "State: " + state.toString())
            _playbackState.postValue(state)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            mediaController.metadata
            _currentlyPlaying.postValue(metadata)
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)
        }

        override fun onSessionDestroyed() {
            mediaBrowserConnectionCallback.onConnectionSuspended()
        }
    }

    fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(parentId, callback)
    }

    fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }
}
@Suppress("PropertyName")
val EMPTY_PLAYBACK_STATE: PlaybackStateCompat = PlaybackStateCompat.Builder()
    .setState(PlaybackStateCompat.STATE_NONE, 0, 0f)
    .build()
