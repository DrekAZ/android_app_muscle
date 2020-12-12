package com.drekaz.muscle.ui.training

import android.util.Log
import androidx.lifecycle.*
import com.drekaz.muscle.database.BodyInfoDatabase
import com.drekaz.muscle.database.TrainingDatabase
import com.drekaz.muscle.database.UserDatabase
import com.drekaz.muscle.database.entity.BodyInfoEntity
import com.drekaz.muscle.database.entity.TrainingEntity
import com.drekaz.muscle.database.entity.UserEntity
import kotlinx.coroutines.launch
import java.time.LocalDate

class TrainingViewModel: ViewModel() {
    val counter: MutableLiveData<Int> = MutableLiveData<Int>(0)
    val setNum: MutableLiveData<Int> = MutableLiveData<Int>(0)
    val restTimeMax: MutableLiveData<Int> = MutableLiveData<Int>(10)
    val restTime: MutableLiveData<Int> = MutableLiveData<Int>(10)
    val nowRest: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)


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

    fun saveData(menu: String, database: TrainingDatabase) {
        val trainingEntity = TrainingEntity(0, menu, counter.value!!, setNum.value!!, LocalDate.now(), 0)
        viewModelScope.launch {
            val dao = database.trainingDao()
            dao.insertTraining(trainingEntity)
            Log.v("TAG", "after insert ${dao.readAll().toString()}")
            database.close()
        }
    }

    suspend fun readBodyInfoData(database: BodyInfoDatabase): BodyInfoEntity {
        val dao = database.bodyInfoDao()
        val myData = dao.readTodayBody()
        database.close()
        return myData
    }
}
