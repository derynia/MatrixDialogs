package com.matrixdialogs.playbackservice

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.matrixdialogs.data.entity.Dialog

class MediaSource(private val songList : List<Dialog>) {
    var tracks = emptyList<MediaMetadataCompat>()

    fun fetchData() {
        tracks = songList.map { dialog ->
            Builder()
                .putString(METADATA_KEY_MEDIA_ID, dialog.item_id.toString())
                .putString(METADATA_KEY_TITLE, dialog.name)
                .putString(METADATA_KEY_DISPLAY_TITLE, dialog.name)
                .putString(METADATA_KEY_MEDIA_URI, dialog.fileName)
                .putString(METADATA_KEY_DISPLAY_DESCRIPTION, dialog.text)
                .putString(METADATA_KEY_DISPLAY_SUBTITLE, "${dialog.languageFrom} -> ${dialog.languageTo}")
                .build()
        }
    }

    fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory) : ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        tracks.forEach { track ->
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(track.getString(METADATA_KEY_MEDIA_URI).toUri()))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }

        return concatenatingMediaSource
    }

    fun asMediaItems() = tracks.map { track ->
        val desc = MediaDescriptionCompat.Builder()
            .setMediaId(track.getString(METADATA_KEY_MEDIA_URI))
            .setTitle(track.description.title)
            .setSubtitle(track.description.subtitle)
            .setMediaId(track.description.mediaId)
            .build()

        MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)
    }
}