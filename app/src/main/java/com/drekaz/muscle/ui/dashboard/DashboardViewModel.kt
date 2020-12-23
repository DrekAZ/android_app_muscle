package com.drekaz.muscle.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drekaz.muscle.calc.CalcData
import com.drekaz.muscle.database.BodyInfoDatabase
import com.drekaz.muscle.database.CaloriesDatabase
import com.drekaz.muscle.database.InitEntity
import com.drekaz.muscle.database.UserDatabase
import com.drekaz.muscle.database.entity.BodyInfoEntity
import com.drekaz.muscle.database.entity.CaloriesEntity
import com.drekaz.muscle.database.entity.UserEntity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.LocalDate
import kotlin.coroutines.coroutineContext

class DashboardViewModel : ViewModel() {
    private val initDatas = InitEntity()
    val userData = MutableLiveData<UserEntity>()
    val todayCalories = MutableLiveData<CaloriesEntity>()
    val weekCalories = MutableLiveData<MutableList<CaloriesEntity>>()
    val latestBodyInfo = MutableLiveData<BodyInfoEntity>()
    val weekBodyInfo = MutableLiveData<MutableList<BodyInfoEntity>>()
    val bmi = MutableLiveData<Float>()

    suspend fun readUserData(database: UserDatabase) = withContext(coroutineContext) {
        val dao = database.userDao()
        userData.value = dao.readMyData(0)
    }

    suspend fun readTodayCalories(database: CaloriesDatabase) = withContext(coroutineContext) {
        val date = LocalDate.now()
        val dao = database.caloriesDao()
        todayCalories.value = dao.readDayCalories(date)
        if(todayCalories.value == null) todayCalories.value = initDatas.caloriesTestData
    }
    suspend fun readWeekCalories(database: CaloriesDatabase) = withContext(coroutineContext) {
        val now = LocalDate.now()
        val dao = database.caloriesDao()
        weekCalories.value = dao.readWeekCalories(now.minusDays(7), now).toMutableList()
        if(weekCalories.value == null){
            weekCalories.value = MutableList(7) { initDatas.caloriesTestData }
        }
        if(weekCalories.value!!.size < 7) {
            for(i in weekCalories.value!!.size..6 ) {
                weekCalories.value!!.add(initDatas.caloriesTestData)
            }
        }
    }

    suspend fun latestBodyInfo(database: BodyInfoDatabase) = withContext(coroutineContext) {
        val dao = database.bodyInfoDao()
        latestBodyInfo.value = dao.readLatestBody()
    }

    suspend fun readWeekBodyInfo(database: BodyInfoDatabase) = withContext(coroutineContext) {
        val now = LocalDate.now()
        val dao = database.bodyInfoDao()
        weekBodyInfo.value = dao.readWeekBody(now.minusDays(7), now).toMutableList()
        if(weekBodyInfo.value == null){
            weekBodyInfo.value = MutableList(7) { initDatas.bodyInfoTestData }
        }
        else if(weekBodyInfo.value!!.size < 7) {
            for(i in weekBodyInfo.value!!.size..6 ) {
                weekBodyInfo.value!!.add(initDatas.bodyInfoTestData)
            }
        }
    }

    suspend fun calcBmi() = withContext(coroutineContext){
        println(userData.value)
        println(latestBodyInfo.value)
        println(weekBodyInfo.value)
        println(weekCalories.value)
        if(latestBodyInfo.value != null) {
            bmi.postValue(CalcData().calcBMI(latestBodyInfo.value!!.weight, latestBodyInfo.value!!.height * 0.01f ))
        }
    }
}