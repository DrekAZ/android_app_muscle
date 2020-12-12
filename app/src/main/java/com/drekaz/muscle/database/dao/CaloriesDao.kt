package com.drekaz.muscle.database.dao

import androidx.room.*
import com.drekaz.muscle.database.entity.CaloriesEntity
import java.time.LocalDate

@Dao
interface CaloriesDao {
    @Insert
    suspend fun insertCalories(calories: CaloriesEntity)

    @Update
    suspend fun updateCalories(calories: CaloriesEntity)

    @Delete
    suspend fun deleteCalories(calories: CaloriesEntity)

    @Query("select * from calories where date = :day")
    suspend fun readDayCalories(day: LocalDate): CaloriesEntity

    @Query("select * from calories where :beforeDay >= date and date <= :nowDay ")
    suspend fun readWeekCalories(beforeDay: LocalDate, nowDay: LocalDate): List<CaloriesEntity>

    @Query("select * from calories where :firstDay >= date and date <= :lastDay")
    suspend fun readMonthCalories(firstDay: LocalDate, lastDay: LocalDate): List<CaloriesEntity>
}