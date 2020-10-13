package com.omarahmed.myassistant.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class ScheduleAlarm {
    companion object{
        fun startAlarm(
            context: Context,
            requestId: Int,
            notificationDate: Long,
            code: String,
            deadline: String,
            type: String
        ){
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context,AlarmReceiver::class.java)
            intent.apply {
                putExtra("code",code)
                putExtra("deadline",deadline)
                putExtra("type",type)
            }
            val pendingIntent = PendingIntent.getBroadcast(context,requestId,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,notificationDate,pendingIntent)
        }

        fun cancelAlarm(
            context: Context,
            requestId: Int
        ){
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context,AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context,requestId,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.cancel(pendingIntent)
        }
    }
}