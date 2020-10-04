package com.omarahmed.myassistant.notification

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.omarahmed.myassistant.R
import com.omarahmed.myassistant.utils.Constants.Companion.CHANNEL_ID

class Notification {
    companion object{
        fun showNotification(context: Context, title: String, body:String){
            val notificationBuilder = NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_test)
                .setContentTitle(title)
                .setContentText(body)
            val notificationManager = NotificationManagerCompat.from(context.applicationContext)
            notificationManager.notify(1,notificationBuilder.build())
        }
    }
}