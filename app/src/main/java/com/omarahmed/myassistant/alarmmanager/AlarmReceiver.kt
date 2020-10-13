package com.omarahmed.myassistant.alarmmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.omarahmed.myassistant.R
import com.omarahmed.myassistant.utils.Constants
import com.omarahmed.myassistant.utils.Constants.Companion.NOTIFICATION_ID

class AlarmReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent?) {
        val code = intent?.getStringExtra("code")
        val deadline = intent?.getStringExtra("deadline")
        val notificationType = intent?.getStringExtra("type")
        val message = if (notificationType == "assignment"){
            "Your ($code) $notificationType will due in $deadline"
        } else {
            "Your ($code) $notificationType will be in $deadline"
        }

        showNotification(context,message)
    }

    private fun showNotification(context: Context, body:String?){
        val notificationBuilder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_test)
            .setContentTitle("Attention")
            .setContentText(body)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID,notificationBuilder.build())
        NOTIFICATION_ID++
    }
}