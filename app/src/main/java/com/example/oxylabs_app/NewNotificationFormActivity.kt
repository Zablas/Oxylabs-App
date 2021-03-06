package com.example.oxylabs_app

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_new_notification_form.*
import java.text.SimpleDateFormat
import java.util.*

class NewNotificationFormActivity : AppCompatActivity() {
    private val database: NotificationDatabaseHelper = NotificationDatabaseHelper(this)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_notification_form)
        txtTime.keyListener = null
    }

    fun onCancelClicked(view: View) = onBackPressed()

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun onAddClicked(view: View){
        clearErrors()
        if (ViewUtils.areFieldsValid(txtTitle, txtTime, txtDescription, resources)){
            val insertResult = saveNotificationToDatabase()
            if (wasInsertionSuccessful(insertResult)) {
                val pendingIntent = createPendingIntent(insertResult)
                setAlarmManager(pendingIntent)
            }
        }
    }

    private fun clearErrors() {
        txtTitle.error = null
        txtTime.error = null
        txtDescription.error = null
    }

    private fun saveNotificationToDatabase(): Long {
        return database.addNewNotification(
            txtTitle.text.trim().toString(),
            txtDescription.text.trim().toString(),
            txtTime.text.toString()
        )
    }

    private fun createPendingIntent(insertResult: Long): PendingIntent {
        val intent = Intent(this, NotificationBroadcaster::class.java)
        intent.putExtra("title", txtTitle.text.trim().toString())
        intent.putExtra("description", txtDescription.text.trim().toString())
        intent.putExtra("id", insertResult)
        return PendingIntent.getBroadcast(this, insertResult.toInt(), intent, 0)
    }

    private fun setAlarmManager(pendingIntent: PendingIntent) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val formatter = SimpleDateFormat(DateTime.DATE_TIME_FORMAT, Locale.US)
        val parsedDateTime = formatter.parse(txtTime.text.toString())
        parsedDateTime?.time?.let { alarmManager.set(AlarmManager.RTC_WAKEUP, it, pendingIntent) }
    }

    private fun wasInsertionSuccessful(insertResult: Long): Boolean = insertResult != -1L

    @RequiresApi(Build.VERSION_CODES.N)
    fun pickDateTime(view: View) {
        val currentDateTime = DateTime(Calendar.getInstance())
        DatePickerDialog(this, { _, year, month, day ->
            showTimePickerDialog(currentDateTime, year, month, day)
        }, currentDateTime.startYear, currentDateTime.startMonth, currentDateTime.startDay).show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showTimePickerDialog(dateTime: DateTime, year: Int, month: Int, day: Int) {
        TimePickerDialog(this, { _, hour, minute -> setTimeEditText(year, month, day, hour, minute) },
            dateTime.startHour, dateTime.startMinute, true).show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setTimeEditText(year: Int, month: Int, day: Int, hour: Int, minute: Int) {
        val pickedDateTime = Calendar.getInstance()
        pickedDateTime.set(year, month, day, hour, minute)
        val formatter = SimpleDateFormat(DateTime.DATE_TIME_FORMAT, Locale.US)
        txtTime.setText(formatter.format(pickedDateTime.time))
    }
}
