package com.drekaz.muscle.database.dao

import androidx.room.*
import com.drekaz.muscle.database.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query ("select * from user where id = :id")
    suspend fun readMyData(id: Int): UserEntity
}