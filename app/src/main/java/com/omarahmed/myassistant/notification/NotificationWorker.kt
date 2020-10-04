package com.omarahmed.myassistant.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, params: WorkerParameters)
    : CoroutineWorker(context,params){
    override suspend fun doWork(): Result {
        return try {
            Notification.showNotification(applicationContext,"title","body")
            Result.success()
        }catch (t:Throwable){
            Result.retry()
        }
    }

}
