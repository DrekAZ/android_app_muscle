package com.drekaz.muscle.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.drekaz.muscle.converter.DateConverter
import java.time.LocalDate

@Entity(tableName = "body_info")
@TypeConverters(DateConverter::class)
data class BodyInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "height") val height: Float,
    @ColumnInfo(name = "weight") val weight: Float,
    @ColumnInfo(name = "fat") val fat: Float,
    @ColumnInfo(name = "date") val date: LocalDate
)