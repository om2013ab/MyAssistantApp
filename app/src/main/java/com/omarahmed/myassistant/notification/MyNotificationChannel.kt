package com.omarahmed.myassistant.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.*
import com.omarahmed.myassistant.utils.Constants
import com.omarahmed.myassistant.utils.Constants.Companion.WORK_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MyNotificationChannel : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()
        setupNotificationChannel()
        workManager()

    }

    private fun workManager() {
        applicationScope.launch {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .build()
            val periodicRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
                1,TimeUnit.DAYS
            )
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
                WORK_NAME, ExistingPeriodicWorkPolicy.KEEP,periodicRequest
            )
        }
    }

    private fun setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.CHANNEL_ID,
                "Notification Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "This channel is for assignments and tests"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}