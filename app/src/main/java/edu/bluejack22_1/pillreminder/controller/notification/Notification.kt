package edu.bluejack22_1.pillreminder.controller.notification

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import edu.bluejack22_1.pillreminder.R

const val notifId = 1
const val chanId = "chan1"
const val titleExtra = "title"
const val messageExtra = "message"


class Notification : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        TODO("Not yet implemented")
        val notification : Notification = NotificationCompat.Builder(context, chanId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notifId, notification)

    }
}