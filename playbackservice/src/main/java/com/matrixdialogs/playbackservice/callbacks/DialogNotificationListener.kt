package com.matrixdialogs.playbackservice.callbacks

import android.app.Notification
import android.content.Intent
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.matrixdialogs.core.NOTIFICATION_ID
import com.matrixdialogs.playbackservice.PlayBackService

class DialogNotificationListener(
    private val playBackService : PlayBackService

) : PlayerNotificationManager.NotificationListener {
    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)
        playBackService.apply {
            stopForeground(true)
            isForegroundService = false
            stopSelf()
        }
    }

    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        super.onNotificationPosted(notificationId, notification, ongoing)
        playBackService.apply {
            if (ongoing && !isForegroundService) {
                ContextCompat.startForegroundService(
                    this,
                    Intent(applicationContext, PlayBackService::class.java)
                )
                startForeground(NOTIFICATION_ID, notification)
                isForegroundService = true
            }
        }
    }
}