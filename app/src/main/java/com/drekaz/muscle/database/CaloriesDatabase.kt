package com.drekaz.muscle.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.drekaz.muscle.converter.DateConverter
import com.drekaz.muscle.database.dao.CaloriesDao
import com.drekaz.muscle.database.entity.CaloriesEntity

@Database(entities = [CaloriesEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class CaloriesDatabase: RoomDatabase() {
    abstract fun caloriesDao(): CaloriesDao

    companion object {
        @Volatile
        private var instance: CaloriesDatabase? = null
        private const val databaseName = "calories_database"
        fun getInstance(context: Context): CaloriesDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, CaloriesDatabase::class.java, databaseName).build()
            }
        }
    }
}