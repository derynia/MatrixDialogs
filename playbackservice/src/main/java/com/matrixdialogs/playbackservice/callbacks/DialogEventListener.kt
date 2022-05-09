package com.matrixdialogs.playbackservice.callbacks

import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.widget.Toast
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.matrixdialogs.core.MEDIA_REPEATS_KEY
import com.matrixdialogs.playbackservice.PlayBackService
import com.matrixdialogs.playbackservice.R

class DialogEventListener(
    private val playBackService: PlayBackService
) : Player.Listener {

    private val player = playBackService.exoPlayer

    override fun onPlaybackStateChanged(playbackState: Int) {
        if (playbackState == Player.STATE_READY && !player.playWhenReady) {
            playBackService.stopForeground(false)
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(playBackService, playBackService.baseContext.getString(R.string.unknown_player_error), Toast.LENGTH_LONG).show()
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        if (player.playbackState == PlaybackStateCompat.STATE_PLAYING) {
            var cRepeats = mediaItem?.mediaMetadata?.extras?.getLong(MEDIA_REPEATS_KEY) ?: 0
            if (cRepeats <= 0L) {
//                mediaItem?.mediaMetadata?.extras?.putLong(MEDIA_REPEATS_KEY, cRepeats)
                if (player.hasNextMediaItem()) {
                    player.seekToNext()
                } else {
                    playBackService.exoPlayer.pause()
                }
            } else {
                cRepeats--
                mediaItem?.mediaMetadata?.extras?.putLong(MEDIA_REPEATS_KEY, cRepeats)
            }
        }

        super.onMediaItemTransition(mediaItem, reason)
    }
}