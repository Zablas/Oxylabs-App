package com.example.oxylabs_app

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_edit_notification.*

class EditNotificationActivity : AppCompatActivity() {
    private var notification: NotificationDTO? = null
    private val database: NotificationDatabaseHelper = NotificationDatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_notification)
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

    fun displayConfirmDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Cancel ${notification?.title}?")
        dialogBuilder.setMessage("Are you sure you want to cancel ${notification?.title}?")
        dialogBuilder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            database.deleteNotification(notification?.id.toString())
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        dialogBuilder.setNegativeButton("No") { _: DialogInterface, _: Int -> }
        dialogBuilder.create().show()
    }
}
