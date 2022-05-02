package com.matrixdialogs.playbackservice.callbacks

import android.widget.Toast
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.matrixdialogs.playbackservice.PlayBackService

class DialogEventListener(
    private val playBackService: PlayBackService
) : Player.Listener {

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_READY && !playWhenReady) {
            playBackService.stopForeground(false)
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(playBackService, "An unknown error occured", Toast.LENGTH_LONG).show()
    }
}