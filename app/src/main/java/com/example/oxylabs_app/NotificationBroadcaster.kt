package com.example.oxylabs_app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationBroadcaster : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("title")
        val description = intent?.getStringExtra("description")
        val id = intent?.getIntExtra("id", -1)
        val notificationBuilder = context?.let {
            NotificationCompat.Builder(it, "TestChannel")
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }

        val notificationManager = context?.let { NotificationManagerCompat.from(it) }
        if (notificationBuilder != null) id?.let { notificationManager?.notify(it, notificationBuilder.build()) }
    }
}