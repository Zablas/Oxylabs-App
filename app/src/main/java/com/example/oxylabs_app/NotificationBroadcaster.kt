package com.example.oxylabs_app

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationBroadcaster() : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onReceive(context: Context?, intent: Intent?) {
        val notification = constructNotification(context, intent)
        if (notification != null) {
            val id = intent?.getLongExtra("id", -1)?.toInt()
            notifyNotificationManager(context, notification, id)
            removeNotificationFromDatabase(context, id)
        }
    }

    private fun constructNotification(context: Context?, intent: Intent?): Notification? {
        val title = intent?.getStringExtra("title")
        val description = intent?.getStringExtra("description")
        return context?.let {
            NotificationCompat.Builder(it, "TestChannel")
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()
        }
    }

    private fun notifyNotificationManager(context: Context?, notification: Notification, id: Int?) {
        val notificationManager = context?.let { NotificationManagerCompat.from(it) }
        if (id != null) notificationManager?.notify(id, notification)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun removeNotificationFromDatabase(context: Context?, id: Int?) {
        val database = NotificationDatabaseHelper(context)
        id?.toString()?.let { database.deleteNotification(it, false) }
    }
}
