package com.drekaz.muscle.converter

import androidx.room.TypeConverter
import java.time.LocalDate

class DateConverter {
    @TypeConverter
    fun fromLocalDate(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun dateToString(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
}