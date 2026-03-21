package com.ranto.devvibe.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.ranto.devvibe.R

class TaskReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val title = intent.getStringExtra("title") ?: "Tâche"
        val description = intent.getStringExtra("description") ?: ""

        val channelId = "task_channel"

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Task Reminder",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("⏰ It's time!")
            .setContentText("$title - $description")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}