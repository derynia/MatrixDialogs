package com.matrixdialogs.playbackservice.service

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.matrixdialogs.core.CHANNEL_ID
import com.matrixdialogs.core.NOTIFICATION_ID
import com.matrixdialogs.playbackservice.R

class DialogNotificationManager(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener,
    private val newTrackCallBack: () -> Unit
) {
    private inner class DescriptionAdapter(
        private val mediaController: MediaControllerCompat
    ) : PlayerNotificationManager.MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): CharSequence {
            newTrackCallBack()
            return mediaController.metadata.description.title.toString()
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent?  = mediaController.sessionActivity

        override fun getCurrentContentText(player: Player): CharSequence
                = mediaController.metadata.description.subtitle.toString()

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? = ContextCompat.getDrawable(context, R.drawable.music)?.toBitmap()
    }

    private val notificationManager : PlayerNotificationManager

    init {
        val mediaControllerCompat = MediaControllerCompat(context, sessionToken)
        notificationManager = PlayerNotificationManager.Builder(context, NOTIFICATION_ID, CHANNEL_ID)
            .setChannelNameResourceId(R.string.notification_Channel_name)
            .setChannelDescriptionResourceId(R.string.notification_Channel_Description)
            .setMediaDescriptionAdapter(DescriptionAdapter(mediaControllerCompat))
            .setNotificationListener(notificationListener)
            .build()
            .apply {
                setSmallIcon(R.drawable.music)
                setMediaSessionToken(sessionToken)
            }
    }

    fun showNotification(player: Player) {
        notificationManager.setPlayer(player)
    }
}