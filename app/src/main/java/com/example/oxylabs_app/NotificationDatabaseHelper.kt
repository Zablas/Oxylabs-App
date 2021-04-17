package com.example.oxylabs_app

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class NotificationDatabaseHelper(
    private val context: Context?
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        const val DATABASE_NAME = "notifications.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "pending_notifications"
        const val COLUMN_ID = "_id"
        const val COLUMN_TITLE = "notification_title"
        const val COLUMN_DESCRIPTION = "notification_description"
        const val COLUMN_SCHEDULED_TIME = "notification_scheduled_time"
    }

    override fun onCreate(database: SQLiteDatabase?) {
        val query = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TITLE TEXT, $COLUMN_DESCRIPTION TEXT, $COLUMN_SCHEDULED_TIME TEXT);"
        database?.execSQL(query)
    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        database?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(database)
    }

    fun addNewNotification(title: String, description: String, scheduledTime: String) {
        val contentValues = constructContentValues(title, description, scheduledTime)
        val result = writableDatabase.insert(TABLE_NAME, null, contentValues)
        displayResultToUser(result)
    }

    private fun displayResultToUser(result: Long) {
        if (result == -1L) Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show()
        else Toast.makeText(context, "Operation successful", Toast.LENGTH_SHORT).show()
    }

    fun getAllNotifications(): Cursor {
        val query = "SELECT * FROM $TABLE_NAME"
        return readableDatabase.rawQuery(query, null)
    }

    fun updateNotificationData(id: String, title: String, description: String, scheduledTime: String) {
        val contentValues = constructContentValues(title, description, scheduledTime)
        val result = writableDatabase.update(TABLE_NAME, contentValues, "$COLUMN_ID=?", arrayOf(id))
        displayResultToUser(result.toLong())
    }

    private fun constructContentValues(title: String, description: String, scheduledTime: String): ContentValues {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TITLE, title)
        contentValues.put(COLUMN_DESCRIPTION, description)
        contentValues.put(COLUMN_SCHEDULED_TIME, scheduledTime)
        return contentValues
    }
}
