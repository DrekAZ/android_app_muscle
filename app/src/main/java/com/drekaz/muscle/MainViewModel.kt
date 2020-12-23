package com.drekaz.muscle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drekaz.muscle.database.BodyInfoDatabase
import com.drekaz.muscle.database.CaloriesDatabase
import com.drekaz.muscle.database.UserDatabase
import com.drekaz.muscle.database.entity.BodyInfoEntity
import com.drekaz.muscle.database.entity.CaloriesEntity
import com.drekaz.muscle.database.entity.UserEntity
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainViewModel : ViewModel() {
    suspend fun readMyData(database: UserDatabase): UserEntity? {
        val dao = database.userDao()
        return dao.readMyData(0)
    }

    fun insertInitCalories(database: CaloriesDatabase) {
        viewModelScope.launch {
            val dao = database.caloriesDao()
            val latestEntity = dao.readLatestCalories()
            val now = LocalDate.now()
            if(latestEntity != null && latestEntity.date != now) {
                val todayEntity = CaloriesEntity(0, 0f, now)
                dao.insertCalories(todayEntity)
            }
        }
    }

    fun insertInitBodyInfo(database: BodyInfoDatabase) {
        viewModelScope.launch {
            val dao = database.bodyInfoDao()
            val latestEntity = dao.readLatestBody()
            val now = LocalDate.now()
            if(latestEntity != null && latestEntity.date != now) {
                val todayEntity = BodyInfoEntity(0, latestEntity.height, latestEntity.weight, latestEntity.fat, now)
                dao.insertBodyInfo(todayEntity)
            }
        }
    }
}