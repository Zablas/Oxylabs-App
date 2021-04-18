package com.example.oxylabs_app

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_new_notification_form.*

class NewNotificationFormActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_notification_form)
        createNotificationChannel()
    }

    fun onCancelClicked(view: View) = onBackPressed()

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun onAddClicked(view: View){
        val database = NotificationDatabaseHelper(this)
        val insertResult = database.addNewNotification(
            txtTitle.text.toString(),
            txtDescription.text.toString(),
            txtTime.text.toString()
        )
        val intent = Intent(this, NotificationBroadcaster::class.java)
        intent.putExtra("title", txtTitle.text.toString())
        intent.putExtra("description", txtDescription.text.toString())
        intent.putExtra("id", insertResult)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val clickTime = System.currentTimeMillis()
        alarmManager.set(AlarmManager.RTC_WAKEUP, clickTime + 5000, pendingIntent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel() {
        val channel = NotificationChannel("TestChannel",
            "TestChannelName",
            NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "TestDescription"
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}
