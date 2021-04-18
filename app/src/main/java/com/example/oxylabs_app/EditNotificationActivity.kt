package com.example.oxylabs_app

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
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
            Toast.makeText(this, R.string.data_transfer_error, Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }

    private fun setEditTextValues() {
        txtTitleEdit.setText(notification?.title)
        txtDescriptionEdit.setText(notification?.description)
        txtTimeEdit.setText(notification?.scheduledTime)
    }

    fun onSaveClicked(view: View) {
        val cursor = database.getNotification(notification?.id.toString())
        if (isNotificationInDatabase(cursor)) {
            updateNotificationOnTheManager()
            saveUpdatedNotificationToDatabase()
            onBackPressed()
        }
        else Toast.makeText(this, R.string.notification_expired, Toast.LENGTH_SHORT).show()
    }

    private fun updateNotificationOnTheManager() {
        notification?.id?.let { cancelNotification(it) }
        setNewNotification()
    }

    private fun setNewNotification() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val parsedDateTime = formatter.parse(txtTimeEdit.text.toString())
        val intent = Intent(this, NotificationBroadcaster::class.java)
        intent.putExtra("title", notification?.title)
        intent.putExtra("description", notification?.description)
        intent.putExtra("id", notification?.id)
        val pendingIntent = notification?.id?.let {
            PendingIntent.getBroadcast(this,
                it, intent, 0)
        }
        parsedDateTime?.time?.let { alarmManager.set(AlarmManager.RTC_WAKEUP, it, pendingIntent) }
    }

    private fun saveUpdatedNotificationToDatabase() {
        database.updateNotificationData(
            notification?.id.toString(),
            txtTitleEdit.text.toString(),
            txtDescriptionEdit.text.toString(),
            txtTimeEdit.text.toString()
        )
    }

    fun onBackClicked(view: View) = onBackPressed()

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun onDeletePressed(view: View) {
        val cursor = database.getNotification(notification?.id.toString())
        if (isNotificationInDatabase(cursor)) displayConfirmDialog()
        else Toast.makeText(this, R.string.notification_expired, Toast.LENGTH_SHORT).show()
    }

    private fun isNotificationInDatabase(cursor: Cursor): Boolean = cursor.moveToNext()

    private fun displayConfirmDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("${R.string.cancel_notification_title} ${notification?.title}?")
            .setMessage("${R.string.cancel_notification_description} ${notification?.title}?")
            .setPositiveButton(R.string.yes_button) { _: DialogInterface, _: Int ->
                notification?.id?.let {
                    cancelNotification(it)
                    database.deleteNotification(it.toString())
                }
                returnToNotificationList()
            }
            .setNegativeButton(R.string.no_button) { _: DialogInterface, _: Int -> }
            .create().show()
    }

    private fun cancelNotification(id: Int) {
        val intent = Intent(this, NotificationBroadcaster::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
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
