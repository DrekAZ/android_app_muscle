package com.drekaz.muscle.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "height") val height: Float,
    @ColumnInfo(name = "weight") val weight: Float,
    @ColumnInfo(name = "fat") val fat: Float,
    @ColumnInfo(name = "sex") val sex: Int,
)