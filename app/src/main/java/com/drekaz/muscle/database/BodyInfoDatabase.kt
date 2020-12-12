package com.drekaz.muscle.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.drekaz.muscle.converter.DateConverter
import com.drekaz.muscle.database.dao.BodyInfoDao
import com.drekaz.muscle.database.entity.BodyInfoEntity

@Database(entities = [BodyInfoEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class BodyInfoDatabase: RoomDatabase() {
    abstract fun bodyInfoDao(): BodyInfoDao

    companion object {
        @Volatile
        private var instance: BodyInfoDatabase? = null
        private const val databaseName = "bodyinfo_database"
        fun getInstance(context: Context): BodyInfoDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, BodyInfoDatabase::class.java, databaseName).build()
            }
        }
    }
}