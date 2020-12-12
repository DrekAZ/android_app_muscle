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
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

class DashboardViewModel : ViewModel() {
    private val initDatas = InitEntity()
    val userData = MutableLiveData<UserEntity>()
    val dayCalories = MutableLiveData<CaloriesEntity>()
    val weekCalories = MutableLiveData<MutableList<CaloriesEntity>>()
    val dayBodyInfo = MutableLiveData<BodyInfoEntity>()
    val weekBodyInfo = MutableLiveData<MutableList<BodyInfoEntity>>()
    val bmi = MutableLiveData<Float>()

    fun readUserData(database: UserDatabase) {
        runBlocking {
            viewModelScope.launch {
                val dao = database.userDao()
                userData.value = dao.readMyData(0)
                database.close()
            }
        }
    }

    fun readDayCalories(database: CaloriesDatabase) {
        val date = LocalDate.now()
        viewModelScope.launch {
            val dao = database.caloriesDao()
            dayCalories.value = dao.readDayCalories(date)
            database.close()
        }
        if(dayCalories.value == null) dayCalories.value = initDatas.caloriesTestData
    }
    fun readWeekCalories(database: CaloriesDatabase) {
        val now = LocalDate.now()
        viewModelScope.launch {
            val dao = database.caloriesDao()
            weekCalories.value = dao.readWeekCalories(now.minusDays(7), now).toMutableList()
            database.close()
        }
        if(weekCalories.value == null){
           //weekCalories.value = MutableList(7) { initDatas.caloriesTestData }
            val l = LocalDate.now()
           weekCalories.value = MutableList(7) { CaloriesEntity(0,1f,l) }
        }
        if(weekCalories.value!!.size < 7) {
            for(i in weekCalories.value!!.size..6 ) {
                weekCalories.value!!.add(initDatas.caloriesTestData)
            }
        }
    }

    fun readDayBodyInfo(database: BodyInfoDatabase) {
        viewModelScope.launch {
            val dao = database.bodyInfoDao()
            dayBodyInfo.value = dao.readTodayBody()
            database.close()
        }
    }
    fun readWeekBodyInfo(database: BodyInfoDatabase) {
        val now = LocalDate.now()
        viewModelScope.launch {
            val dao = database.bodyInfoDao()
            weekBodyInfo.value = dao.readWeekBody(now.minusDays(7), now).toMutableList()
            database.close()
        }
        if(weekBodyInfo.value == null){
            val l = LocalDate.now()
            weekBodyInfo.value = MutableList(7) { BodyInfoEntity(0,0f,10.2f,0f,l) }
        }
        else if(weekBodyInfo.value!!.size < 7) {
            for(i in weekBodyInfo.value!!.size..6 ) {
                weekBodyInfo.value!!.add(initDatas.bodyInfoTestData)
            }
        }
    }

    fun calcBmi() {
        if(userData.value != null) {
            bmi.postValue(CalcData().calcBMI(dayBodyInfo.value!!.weight, dayBodyInfo.value!!.height))
        }
    }
}