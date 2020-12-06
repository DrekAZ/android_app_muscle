package com.drekaz.muscle.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.drekaz.muscle.database.dao.UserDao
import com.drekaz.muscle.database.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao

    companion object {
        @Volatile
        private var instance: UserDatabase? = null
        private const val databaseName = "user_database"
        fun getInstance(context: Context): UserDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, UserDatabase::class.java, databaseName).build()
            }
        }
    }
}