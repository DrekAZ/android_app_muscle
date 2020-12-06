package com.drekaz.muscle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drekaz.muscle.database.UserDatabase
import com.drekaz.muscle.database.entity.UserEntity
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    suspend fun readMyData(database: UserDatabase): UserEntity? {
        val dao = database.userDao()
        val myData = dao.readMyData(0)
        database.close()
        return myData
    }
}