package com.matrixdialogs.playbackservice.service

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.matrixdialogs.core.MEDIA_REPEATS_KEY
import com.matrixdialogs.data.entity.Dialog

class MediaSource {
    val dialogs : MutableList<Dialog> = mutableListOf()
    var tracks = emptyList<MediaMetadataCompat>()

    fun fetchData() {
        tracks = dialogs.map { dialog ->
            Builder()
                .putString(METADATA_KEY_MEDIA_ID, dialog.item_id.toString())
                .putString(METADATA_KEY_TITLE, dialog.name)
                .putString(METADATA_KEY_DISPLAY_TITLE, dialog.name)
                .putString(METADATA_KEY_MEDIA_URI, dialog.fileName)
                .putString(METADATA_KEY_DISPLAY_DESCRIPTION, dialog.text)
                .putString(METADATA_KEY_DISPLAY_SUBTITLE, "${dialog.languageFrom} -> ${dialog.languageTo}")
                .putString(METADATA_KEY_DISPLAY_SUBTITLE, "${dialog.languageFrom} -> ${dialog.languageTo}")
                .putLong(MEDIA_REPEATS_KEY, dialog.repeats.toLong())
                .build()
        }
    }

    fun clearData() {
        dialogs.clear()
    }

    fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory) : ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        tracks.forEach { track ->
            val mediaItem = MediaItem.Builder()
                .setUri(track.getString(METADATA_KEY_MEDIA_URI).toUri())
                .setMediaMetadata(MediaMetadata.Builder()
                    .setExtras(bundleOf(MEDIA_REPEATS_KEY to track.getLong(MEDIA_REPEATS_KEY)))
                    .build()
                )
                .build()
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaItem)
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
            .setExtras(bundleOf(MEDIA_REPEATS_KEY to track.getLong(MEDIA_REPEATS_KEY)))
            .build()

        MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)
    }.toMutableList()
}