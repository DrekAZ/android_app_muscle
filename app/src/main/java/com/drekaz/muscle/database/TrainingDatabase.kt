package com.drekaz.muscle.database

import android.content.Context
import androidx.room.*
import com.drekaz.muscle.converter.DateConverter
import com.drekaz.muscle.database.dao.TrainingDao
import com.drekaz.muscle.database.entity.TrainingEntity

@Database(entities = [TrainingEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class TrainingDatabase: RoomDatabase() {
    abstract fun trainingDao(): TrainingDao

    companion object {
        @Volatile
        private var instance: TrainingDatabase? = null
        private const val databaseName = "training_database"
        fun getInstance(context: Context): TrainingDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, TrainingDatabase::class.java, databaseName).build()
            }
        }
    }
}