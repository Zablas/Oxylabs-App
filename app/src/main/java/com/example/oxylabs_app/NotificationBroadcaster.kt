package com.example.oxylabs_app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationBroadcaster : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationBuilder = context?.let {
            NotificationCompat.Builder(it, "TestChannel")
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Test title")
                .setContentText("This is a text notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }

        val notificationManager = context?.let { NotificationManagerCompat.from(it) }
        if (notificationBuilder != null) notificationManager?.notify(200, notificationBuilder.build())
    }
}