package com.drekaz.muscle.ui.first_launch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drekaz.muscle.database.BodyInfoDatabase
import com.drekaz.muscle.database.UserDatabase
import com.drekaz.muscle.database.entity.BodyInfoEntity
import com.drekaz.muscle.database.entity.UserEntity
import kotlinx.coroutines.launch
import java.time.LocalDate

class FirstLaunchViewModel : ViewModel() {
    val name = MutableLiveData("")
    val height = MutableLiveData(0.0f)
    val weight = MutableLiveData(0.0f)
    val fat = MutableLiveData(0.0f)
    val sex = MutableLiveData(0)
    val isEnabled = MutableLiveData(false)

    fun checkAll() {
        isEnabled.value = checkName() && checkHeight() && checkWeight() && checkSex()
    }

    fun checkName(): Boolean {
        return name.value!!.length in 1..15
    }
    fun checkHeight(): Boolean {
        return height.value!! in 90.0..250.0
    }
    fun checkWeight(): Boolean {
        return weight.value!! in 10.0..300.0
    }
    fun checkSex(): Boolean {
        return (sex.value!! == 1 || sex.value!! == 2 || sex.value!! == 9)
    }
    fun saveData(userDatabase: UserDatabase, bodyInfoDatabase: BodyInfoDatabase) {
        val userEntity = UserEntity(0,name.value!!, sex.value!!)
        val bodyInfoEntity = BodyInfoEntity(0,height.value!!,weight.value!!,fat.value!!, LocalDate.now())
        viewModelScope.launch {
            userDatabase.userDao().insertUser(userEntity)
            userDatabase.close()
            bodyInfoDatabase.bodyInfoDao().insertBodyInfo(bodyInfoEntity)
            bodyInfoDatabase.close()
        }
    }
}