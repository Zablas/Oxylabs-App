package com.example.oxylabs_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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

    fun onEditClicked(view: View) {
        database.updateNotificationData(
            notification?.id.toString(),
            txtTitleEdit.text.toString(),
            txtDescriptionEdit.text.toString(),
            txtTimeEdit.text.toString()
        )
        onBackPressed()
    }

    fun onCancelClicked(view: View) = onBackPressed()

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
