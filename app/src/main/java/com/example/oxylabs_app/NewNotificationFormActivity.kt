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
    private val database: NotificationDatabaseHelper = NotificationDatabaseHelper(this)

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
        val insertResult = saveNotificationToDatabase()
        if (wasInsertionSuccessful(insertResult)) {
            val pendingIntent = createPendingIntent(insertResult)
            setAlarmManager(pendingIntent)
        }
    }

    private fun saveNotificationToDatabase(): Long {
        return database.addNewNotification(
            txtTitle.text.toString(),
            txtDescription.text.toString(),
            txtTime.text.toString()
        )
    }

    private fun createPendingIntent(insertResult: Long): PendingIntent {
        val intent = Intent(this, NotificationBroadcaster::class.java)
        intent.putExtra("title", txtTitle.text.toString())
        intent.putExtra("description", txtDescription.text.toString())
        intent.putExtra("id", insertResult)
        return PendingIntent.getBroadcast(this, 0, intent, 0)
    }

    private fun setAlarmManager(pendingIntent: PendingIntent) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val clickTime = System.currentTimeMillis()
        alarmManager.set(AlarmManager.RTC_WAKEUP, clickTime + 5000, pendingIntent)
    }

    private fun wasInsertionSuccessful(insertResult: Long): Boolean = insertResult != -1L

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
