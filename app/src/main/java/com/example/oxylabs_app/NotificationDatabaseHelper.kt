package com.example.oxylabs_app

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NotificationDatabaseHelper(
    context: Context?
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

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TITLE TEXT, $COLUMN_DESCRIPTION TEXT, $COLUMN_SCHEDULED_TIME TEXT);"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}