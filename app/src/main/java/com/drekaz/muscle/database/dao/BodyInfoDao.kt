package com.drekaz.muscle.database.dao

import androidx.room.*
import com.drekaz.muscle.database.entity.BodyInfoEntity
import com.drekaz.muscle.database.entity.CaloriesEntity
import java.time.LocalDate

@Dao
interface BodyInfoDao {
    @Insert
    suspend fun insertBodyInfo(body: BodyInfoEntity)

    @Update
    suspend fun updateBodyInfo(body: BodyInfoEntity)

    @Delete
    suspend fun deleteBodyInfo(body: BodyInfoEntity)

    @Query("select * from body_info order by id desc limit 1")
    suspend fun readLatestBody(): BodyInfoEntity

    @Query("select * from body_info where date = :today")
    suspend fun readTodayBody(today: LocalDate): BodyInfoEntity

    @Query("select * from body_info where :beforeDay < date and date <= :nowDay ")
    suspend fun readWeekBody(beforeDay: LocalDate, nowDay: LocalDate): List<BodyInfoEntity>
}