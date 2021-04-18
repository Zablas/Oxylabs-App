package com.example.oxylabs_app

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_edit_notification.*
import java.text.SimpleDateFormat
import java.util.*

class EditNotificationActivity : AppCompatActivity() {
    private var notification: NotificationDTO? = null
    private val database: NotificationDatabaseHelper = NotificationDatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_notification)
        txtTimeEdit.keyListener = null
        getIntentData()
        setEditTextValues()
    }

    private fun getIntentData() {
        if(intent.hasExtra("notification"))
            notification = intent.getSerializableExtra("notification") as NotificationDTO
        else {
            Toast.makeText(this, "Error while transferring data", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }

    private fun setEditTextValues() {
        txtTitleEdit.setText(notification?.title)
        txtDescriptionEdit.setText(notification?.description)
        txtTimeEdit.setText(notification?.scheduledTime)
    }

    fun onSaveClicked(view: View) {
        database.updateNotificationData(
            notification?.id.toString(),
            txtTitleEdit.text.toString(),
            txtDescriptionEdit.text.toString(),
            txtTimeEdit.text.toString()
        )
        onBackPressed()
    }

    fun onBackClicked(view: View) = onBackPressed()

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun onDeletePressed(view: View) {
        displayConfirmDialog()
    }

    private fun displayConfirmDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Cancel ${notification?.title}?")
        dialogBuilder.setMessage("Are you sure you want to cancel ${notification?.title}?")
        dialogBuilder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            notification?.id?.let { cancelNotification(it) }
            returnToNotificationList()
        }
        dialogBuilder.setNegativeButton("No") { _: DialogInterface, _: Int -> }
        dialogBuilder.create().show()
    }

    private fun cancelNotification(id: Int) {
        val intent = Intent(this, NotificationBroadcaster::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        database.deleteNotification(id.toString())
    }

    private fun returnToNotificationList() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun pickDateTime(view: View) {
        val currentDateTime = CurrentDateTime(Calendar.getInstance())
        DatePickerDialog(this, { _, year, month, day ->
            showTimePickerDialog(currentDateTime, year, month, day)
        }, currentDateTime.startYear, currentDateTime.startMonth, currentDateTime.startDay).show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showTimePickerDialog(currentDateTime: CurrentDateTime, year: Int, month: Int, day: Int) {
        TimePickerDialog(this, { _, hour, minute -> setTimeEditText(year, month, day, hour, minute) },
            currentDateTime.startHour, currentDateTime.startMinute, true).show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setTimeEditText(year: Int, month: Int, day: Int, hour: Int, minute: Int) {
        val pickedDateTime = Calendar.getInstance()
        pickedDateTime.set(year, month, day, hour, minute)
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        txtTimeEdit.setText(formatter.format(pickedDateTime.time))
    }
}
