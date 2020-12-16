package com.drekaz.muscle.ui.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drekaz.muscle.database.BodyInfoDatabase
import com.drekaz.muscle.database.InitEntity
import com.drekaz.muscle.database.UserDatabase
import com.drekaz.muscle.database.entity.BodyInfoEntity
import com.drekaz.muscle.database.entity.UserEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.LocalDate
import kotlin.coroutines.coroutineContext

class SettingViewModel : ViewModel() {
    val userData = MutableLiveData<UserEntity>()

    suspend fun readUserData(database: UserDatabase) = viewModelScope.launch {
        val dao = database.userDao()
        userData.value = dao.readMyData(0)

        if (userData.value == null) {
            userData.value = InitEntity().userTestData
        }
    }

    /// SettingFragmentからViewModelのUserDataを書き換え、
    /// updateUser(userData)としないのはViewModelだから
    fun updateUserData(database: UserDatabase, updateData: UserEntity) {
        viewModelScope.launch {
            val dao = database.userDao()
            dao.updateUser(updateData)

            userData.value = updateData
        }
    }

    fun updateBodyInfo(database: BodyInfoDatabase, arrayStr: Array<String>) {
        val insert = setBodyInfoEntity(database, arrayStr)
        println(insert)

        /// id == 0 今日最初のデータ
        if(insert.id == 0) {
            viewModelScope.launch {
                val dao = database.bodyInfoDao()
                dao.insertBodyInfo(insert)
                println("insert")
            }
        } else {
            viewModelScope.launch {
                val dao = database.bodyInfoDao()
                dao.updateBodyInfo(insert)
                println("update")
            }
        }

    }

    /// fragment_setting.xmlの性別表示用
    fun sexNumToStr(sexNum: Int): String {
        when(sexNum) {
            1 -> return "男性"
            2 -> return "女性"
        }
        return "その他"
    }

    private suspend fun getLatestBodyInfo(database: BodyInfoDatabase): BodyInfoEntity {
        var latest: BodyInfoEntity?
        withContext(coroutineContext) {
            val dao = database.bodyInfoDao()
            latest = dao.readLatestBody()
        }
        if(latest == null) latest ?: InitEntity().bodyInfoTestData
        return latest!!
    }

    private fun setBodyInfoEntity(database: BodyInfoDatabase, arrayStr: Array<String>): BodyInfoEntity {
        var latest: BodyInfoEntity
        runBlocking {
            latest = getLatestBodyInfo(database)
        }
        println(latest)

        /// 最新のデータが今日の日付: 更新, 今日以外: 追加
        val id = if(latest.date.toEpochDay() == LocalDate.now().toEpochDay()) latest.id else 0
        val height = if(arrayStr[0].isBlank()) latest!!.height else arrayStr[0].toFloat()
        val weight = if(arrayStr[1].isBlank()) latest!!.weight else arrayStr[1].toFloat()
        val fat = if(arrayStr[2].isBlank()) latest!!.fat else arrayStr[2].toFloat()

        return BodyInfoEntity(id, height, weight, fat, LocalDate.now())
    }
}