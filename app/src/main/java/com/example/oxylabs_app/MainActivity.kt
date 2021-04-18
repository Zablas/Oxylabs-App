package com.example.oxylabs_app

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val database: NotificationDatabaseHelper = NotificationDatabaseHelper(this)
    private val notifications: ArrayList<NotificationDTO> = ArrayList()
    private val notificationAdapter: NotificationAdapter = NotificationAdapter(this, notifications, database)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        displayNotificationList()
        rvNotifications.adapter = notificationAdapter
        rvNotifications.layoutManager = LinearLayoutManager(this)
        swipeContainer.setOnRefreshListener { displayNotificationList() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (isCancelAllClicked(item)) displayConfirmDialog()
        return super.onOptionsItemSelected(item)
    }

    private fun isCancelAllClicked(item: MenuItem): Boolean = item.itemId == R.id.cancel_all

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel() {
        val channel = NotificationChannel("MainNotificationChannel",
            "MainNotificationChannel",
            NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "MainNotificationChannel"
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    private fun displayConfirmDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Cancel all notifications?")
            .setMessage("Are you sure you want to cancel all notifications?")
            .setPositiveButton("Yes") { _: DialogInterface, _: Int -> cancelAllNotifications() }
            .setNegativeButton("No") { _: DialogInterface, _: Int -> }
            .create().show()
    }

    private fun cancelAllNotifications() {
        cancelAllNotificationsFromManager()
        database.deleteAllNotifications()
        refreshAdapter()
        Toast.makeText(this, "All notifications cancelled", Toast.LENGTH_SHORT).show()
    }

    private fun cancelAllNotificationsFromManager() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationBroadcaster::class.java)
        for (notification in notifications) {
            val pendingIntent = PendingIntent.getBroadcast(this, notification.id, intent, 0)
            alarmManager.cancel(pendingIntent)
        }
    }

    private fun refreshAdapter() {
        val notificationCount = notifications.size
        notifications.clear()
        notificationAdapter.notifyItemRangeRemoved(0, notificationCount)
    }

    fun openNewNotificationForm(view: View){
        val intent = Intent(this, NewNotificationFormActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun displayNotificationList(){
        notifications.clear()
        val cursor = database.getAllNotifications()
        while (cursor.moveToNext())
            addNotificationToArray(cursor)
        notificationAdapter.notifyDataSetChanged()
        swipeContainer.isRefreshing = false
    }

    private fun addNotificationToArray(cursor: Cursor){
        val id = cursor.getInt(0)
        val title = cursor.getString(1)
        val description = cursor.getString(2)
        val scheduledTime = cursor.getString(3)
        notifications.add(NotificationDTO(id, title, description, scheduledTime))
    }
}
