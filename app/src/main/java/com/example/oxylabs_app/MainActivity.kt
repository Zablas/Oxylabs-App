package com.example.oxylabs_app

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val database: NotificationDatabaseHelper = NotificationDatabaseHelper(this)
    private val notifications: ArrayList<NotificationDTO> = ArrayList()
    private val notificationAdapter: NotificationAdapter = NotificationAdapter(this, notifications)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        displayNotificationList()
        rvNotifications.adapter = notificationAdapter
        rvNotifications.layoutManager = LinearLayoutManager(this)
    }

    fun openNewNotificationForm(view: View){
        val intent = Intent(this, NewNotificationFormActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun displayNotificationList(){
        val cursor = database.getAllNotifications()
        while (cursor.moveToNext())
            addNotificationToArray(cursor)
    }

    private fun addNotificationToArray(cursor: Cursor){
        val id = cursor.getInt(0)
        val title = cursor.getString(1)
        val description = cursor.getString(2)
        val scheduledTime = cursor.getString(3)
        notifications.add(NotificationDTO(id, title, description, scheduledTime))
    }
}
