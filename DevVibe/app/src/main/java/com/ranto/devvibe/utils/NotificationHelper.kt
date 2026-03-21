package com.ranto.devvibe.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ranto.devvibe.R
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import com.ranto.devvibe.receivers.TaskReminderReceiver
import java.text.SimpleDateFormat
import java.util.*

object NotificationHelper {

    private const val CHANNEL_ID = "pomodoro_channel"

    fun showNotification(context: Context) {
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Pomodoro",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("⏰ Temps écoulé !")
            .setContentText("Ta session est terminée, tâche accomplie ! 🎉")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(1, notification)
    }

    fun scheduleTaskNotification(
        context: Context,
        title: String,
        description: String,
        startTime: String
    ) {

        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = formatter.parse(startTime) ?: return

        val calendar = Calendar.getInstance().apply {
            time = Date()
            set(Calendar.HOUR_OF_DAY, date.hours)
            set(Calendar.MINUTE, date.minutes)
            set(Calendar.SECOND, 0)
        }

        val intent = Intent(context, TaskReminderReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("description", description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}