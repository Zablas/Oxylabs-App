package com.example.oxylabs_app

import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (isCancelAllClicked(item)) displayConfirmDialog()
        return super.onOptionsItemSelected(item)
    }

    private fun isCancelAllClicked(item: MenuItem): Boolean = item.itemId == R.id.cancel_all

    private fun displayConfirmDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Cancel all notifications?")
        dialogBuilder.setMessage("Are you sure you want to cancel all notifications?")
        dialogBuilder.setPositiveButton("Yes") { _: DialogInterface, _: Int -> cancelAllNotifications() }
        dialogBuilder.setNegativeButton("No") { _: DialogInterface, _: Int -> }
        dialogBuilder.create().show()
    }

    private fun cancelAllNotifications() {
        database.deleteAllNotifications()
        val notificationCount = notifications.size
        notifications.clear()
        notificationAdapter.notifyItemRangeRemoved(0, notificationCount)
        Toast.makeText(this, "All notifications cancelled", Toast.LENGTH_SHORT).show()
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
