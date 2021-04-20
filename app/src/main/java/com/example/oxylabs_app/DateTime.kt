package com.example.oxylabs_app

import android.os.Build
import androidx.annotation.RequiresApi
import android.icu.util.*

class DateTime(private val calendar: Calendar) {
    @RequiresApi(Build.VERSION_CODES.N)
    val startYear = calendar.get(Calendar.YEAR)
    @RequiresApi(Build.VERSION_CODES.N)
    val startMonth = calendar.get(Calendar.MONTH)
    @RequiresApi(Build.VERSION_CODES.N)
    val startDay = calendar.get(Calendar.DAY_OF_MONTH)
    @RequiresApi(Build.VERSION_CODES.N)
    val startHour = calendar.get(Calendar.HOUR_OF_DAY)
    @RequiresApi(Build.VERSION_CODES.N)
    val startMinute = calendar.get(Calendar.MINUTE)

    companion object{
        const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }
}
