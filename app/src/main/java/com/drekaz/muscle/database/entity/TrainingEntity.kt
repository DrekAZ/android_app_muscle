package com.drekaz.muscle.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "training")
data class TrainingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "menu") val menu: String,
    @ColumnInfo(name = "counter") val counter: Int,
    @ColumnInfo(name = "set_num") val setNum: Int,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "user_id") val userId: Int
)