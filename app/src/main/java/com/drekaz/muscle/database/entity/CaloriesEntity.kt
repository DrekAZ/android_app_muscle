package com.drekaz.muscle.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.drekaz.muscle.converter.DateConverter
import java.time.LocalDate

@Entity(tableName = "calories")
@TypeConverters(DateConverter::class)
data class CaloriesEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "calories") val calories: Float,
    @ColumnInfo(name = "date") val date: LocalDate
)