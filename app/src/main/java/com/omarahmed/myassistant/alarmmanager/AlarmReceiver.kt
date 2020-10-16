package com.omarahmed.myassistant.alarmmanager

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.omarahmed.myassistant.MainActivity
import com.omarahmed.myassistant.R
import com.omarahmed.myassistant.utils.Constants
import com.omarahmed.myassistant.utils.Constants.Companion.NOTIFICATION_ID

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (Intent.ACTION_BOOT_COMPLETED == intent?.action) {
            val i = Intent(context, RestartAlarm::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(i)
            } else {
                context.startService(i)
            }
        } else {
            val code = intent?.getStringExtra("name")
            val deadline = intent?.getStringExtra("deadline")
            val notificationType = intent?.getStringExtra("type")
            val message = if (notificationType == "assignment") {
                "Your ($code) $notificationType will due in $deadline"
            } else {
                "Your ($code) $notificationType will be in $deadline"
            }


            showNotification(context, message)
        }

    }

    private fun showNotification(context: Context, body: String?) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val notificationBuilder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_assistant)
            .setContentTitle("Attention")
            .setColor(Color.BLUE)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))

            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        NOTIFICATION_ID++
    }
}