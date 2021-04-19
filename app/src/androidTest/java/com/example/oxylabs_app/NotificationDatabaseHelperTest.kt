package com.example.oxylabs_app

import android.content.ContentValues
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import java.sql.SQLException

class NotificationDatabaseHelperTest {
    private lateinit var context: Context
    lateinit var database: NotificationDatabaseHelper
    lateinit var contentValues: ContentValues

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        database = NotificationDatabaseHelper(context)
        contentValues = constructContentValues()
    }

    private fun constructContentValues(): ContentValues {
        val contentValues = ContentValues()
        contentValues.put(NotificationDatabaseHelper.COLUMN_TITLE, "TestCase")
        contentValues.put(NotificationDatabaseHelper.COLUMN_DESCRIPTION, "TestCase")
        contentValues.put(NotificationDatabaseHelper.COLUMN_SCHEDULED_TIME, "1970-01-01 00:00:00")
        return contentValues
    }

    @Test
    fun onCreate() {
        val query = "SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '${NotificationDatabaseHelper.TABLE_NAME}'"
        val cursor = database.readableDatabase.rawQuery(query, null)
        val result = cursor != null && cursor.count > 0
        assertThat(result).isTrue()
    }


    @Test
    fun addNewNotification() {
        val result = database.writableDatabase.insert(NotificationDatabaseHelper.TABLE_NAME, null, contentValues)
        assertThat(result > 0).isTrue()
    }

    @Test
    fun getAllNotifications() {
        database.writableDatabase.insert(NotificationDatabaseHelper.TABLE_NAME, null, contentValues)
        val cursor = database.getAllNotifications()
        assertThat(cursor.count > 0).isTrue()
    }

    @Test
    fun getNotification() {
        val result = database.writableDatabase.insert(NotificationDatabaseHelper.TABLE_NAME, null, contentValues)
        val cursor = database.getNotification(result.toString())
        assertThat(cursor.count == 1).isTrue()
    }

    @Test
    fun updateNotificationData() {
        val id = database.writableDatabase.insert(NotificationDatabaseHelper.TABLE_NAME, null, contentValues)
        val result = database.writableDatabase
            .update(NotificationDatabaseHelper.TABLE_NAME, contentValues,
                "${NotificationDatabaseHelper.COLUMN_ID}=?",
                arrayOf(id.toString()))
        assertThat(result == 1).isTrue()
    }

    @Test
    fun deleteNotification() {
        val id = database.writableDatabase.insert(NotificationDatabaseHelper.TABLE_NAME, null, contentValues)
        val result = database.writableDatabase
            .delete(NotificationDatabaseHelper.TABLE_NAME,
                "${NotificationDatabaseHelper.COLUMN_ID}=?",
                arrayOf(id.toString()))
        assertThat(result == 1).isTrue()
    }

    @Test
    fun deleteAllNotifications() {
        try {
            database.writableDatabase.execSQL("DELETE FROM ${NotificationDatabaseHelper.TABLE_NAME}")
            assert(true)
        }
        catch (ex: SQLException){
            assert(false)
        }
    }
}