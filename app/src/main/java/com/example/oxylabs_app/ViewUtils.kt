package com.example.oxylabs_app

import android.content.res.*
import android.os.Build
import android.widget.EditText
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*

object ViewUtils {
    @RequiresApi(Build.VERSION_CODES.N)
    fun areFieldsValid(titleEdit: EditText,
                               timeEdit: EditText,
                               descriptionEdit: EditText,
                               resources: Resources): Boolean {
        var result = true
        if (titleEdit.text.trim().isEmpty()) {
            titleEdit.error = resources.getString(R.string.title_validation_error)
            result = false
        }
        if (timeEdit.text.isEmpty()) {
            timeEdit.error = resources.getString(R.string.time_validation_error)
            result = false
        }
        else if (isChosenTimeInThePast(timeEdit)) {
            timeEdit.error = resources.getString(R.string.time_in_past_validation_error)
            result = false
        }
        if (descriptionEdit.text.trim().isEmpty()) {
            descriptionEdit.error = resources.getString(R.string.description_validation_error)
            result = false
        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun isChosenTimeInThePast(timeEdit: EditText): Boolean {
        val formatter = SimpleDateFormat(DateTime.DATE_TIME_FORMAT, Locale.US)
        val parsedDateTime = formatter.parse(timeEdit.text.toString())
        val currentTime = System.currentTimeMillis()
        return parsedDateTime?.time!! < currentTime
    }
}