package com.drekaz.muscle.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drekaz.muscle.calc.CalcData
import com.drekaz.muscle.database.CaloriesDatabase
import com.drekaz.muscle.database.InitEntity
import com.drekaz.muscle.database.UserDatabase
import com.drekaz.muscle.database.entity.CaloriesEntity
import com.drekaz.muscle.database.entity.UserEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

class DashboardViewModel : ViewModel() {
    private val initDatas = InitEntity()
    val userData = MutableLiveData<UserEntity>()
    val dayCalories = MutableLiveData<CaloriesEntity>()
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

    fun calcBmi() {
        if(userData.value != null) {
            bmi.postValue(CalcData().calcBMI(userData.value!!.weight, userData.value!!.height))
        }
    }
}