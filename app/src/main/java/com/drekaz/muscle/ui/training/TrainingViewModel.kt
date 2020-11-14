package com.drekaz.muscle.ui.training

import androidx.lifecycle.*
import kotlin.math.max

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
}
