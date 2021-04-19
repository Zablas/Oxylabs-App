package com.example.oxylabs_app

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class NotificationDatabaseHelperTest {
    lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun onCreate() {
        val database = NotificationDatabaseHelper(context)
        val query = "SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '${NotificationDatabaseHelper.TABLE_NAME}'"
        val cursor = database.readableDatabase.rawQuery(query, null)
        val result = cursor != null && cursor.count > 0
        assertThat(result).isTrue()
    }

    @Test
    fun onUpgrade() {

    }

    @Test
    fun addNewNotification() {
    }

    @Test
    fun getAllNotifications() {
    }

    @Test
    fun getNotification() {
    }

    @Test
    fun updateNotificationData() {
    }

    @Test
    fun deleteNotification() {
    }

    @Test
    fun deleteAllNotifications() {
    }
}