package com.drekaz.muscle.ui.training

import androidx.lifecycle.*
import com.drekaz.muscle.calc.CalcData
import com.drekaz.muscle.database.BodyInfoDatabase
import com.drekaz.muscle.database.CaloriesDatabase
import com.drekaz.muscle.database.TrainingDatabase
import com.drekaz.muscle.database.entity.BodyInfoEntity
import com.drekaz.muscle.database.entity.CaloriesEntity
import com.drekaz.muscle.database.entity.TrainingEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import kotlin.coroutines.coroutineContext

class TrainingViewModel: ViewModel() {
    val counter: MutableLiveData<Int> = MutableLiveData<Int>(0)
    val setNum: MutableLiveData<Int> = MutableLiveData<Int>(0)
    val restTimeMax: MutableLiveData<Int> = MutableLiveData<Int>(10)
    val restTime: MutableLiveData<Int> = MutableLiveData<Int>(10)
    val nowRest: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val myBodyInfo = MutableLiveData<BodyInfoEntity>()


    fun countUp() {
        counter.value = counter.value!! + 1
    }
    fun resetCounter() {
        counter.value = 0
    }
    fun countUpSet() {
        setNum.value = setNum.value!! + 1
    }
    fun setRestTimeMax(maxTime: Int) {
        restTimeMax.value = maxTime
    }
    fun setRestTime(time: Int) {
        restTime.value = time
    }
    fun changeRestState() {
        nowRest.value = !nowRest.value!!
    }

    fun saveData(menu: String, trainingDatabase: TrainingDatabase, caloriesDatabase: CaloriesDatabase, trainingHour: Float) {
        val trainingEntity = TrainingEntity(0, menu, counter.value!!, setNum.value!!, LocalDate.now(), 0)
        viewModelScope.launch {
            val trainingDao = trainingDatabase.trainingDao()
            trainingDao.insertTraining(trainingEntity)
            val caloriesDao = caloriesDatabase.caloriesDao()
            val latestCalories = caloriesDao.readLatestCalories()
            val now = LocalDate.now()
            val calories = CalcData().calcCalorie(menu, myBodyInfo.value!!.weight, trainingHour)
            if(latestCalories != null && latestCalories.date == now) {
                caloriesDao.updateCalories(CaloriesEntity(latestCalories.id, calories + latestCalories.calories, now))
            } else {
                caloriesDao.insertCalories(CaloriesEntity(0, calories, now))
            }
        }
    }

    suspend fun readBodyInfoData(database: BodyInfoDatabase) = withContext(coroutineContext) {
        val dao = database.bodyInfoDao()
        myBodyInfo.value = dao.readLatestBody()
    }
}
