package com.drekaz.muscle.database.dao

import androidx.room.*
import com.drekaz.muscle.database.entity.TrainingEntity

@Dao
interface TrainingDao {
    @Insert
    suspend fun insertTraining(training: TrainingEntity)

    @Update
    suspend fun updateTraining(training: TrainingEntity)

    @Delete
    suspend fun deleteTraining(training: TrainingEntity)

    @Query("select * from training")
    suspend fun readAll(): List<TrainingEntity>

    @Query("select * from training where menu = :menu")
    suspend fun searchTraining(menu: String): List<TrainingEntity>
}
