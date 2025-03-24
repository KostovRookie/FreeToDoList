package com.kostov.freetodolist.data.local

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime

class Converters {
    @TypeConverter
    fun fromUrgency(value: Urgency): String = value.name

    @TypeConverter
    fun toUrgency(value: String): Urgency = Urgency.valueOf(value)

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.toString()

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDate(value: String): LocalDate = LocalDate.parse(value)

    @TypeConverter
    fun fromLocalTime(time: LocalTime): String = time.toString()

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalTime(value: String): LocalTime = LocalTime.parse(value)
}