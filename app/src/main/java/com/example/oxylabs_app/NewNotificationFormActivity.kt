package com.example.oxylabs_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_new_notification_form.*

class NewNotificationFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_notification_form)
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
        database.addNewNotification(
            txtTitle.text.toString(),
            txtDescription.text.toString(),
            txtTime.text.toString()
        )
    }
}
