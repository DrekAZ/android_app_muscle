package com.drekaz.muscle.ui.training

import android.hardware.Sensor
import android.hardware.SensorManager

interface InterfaceSensorProcess {

    fun countJudge(sensorValue: Float?, canUsed: Boolean, menuElement: String) : Boolean {
        when (menuElement) {
            "腕立て伏せ", "上体起こし" -> return judgeProximity(sensorValue, canUsed)
        }
        return false
    }

    fun getSensor(sensorManager: SensorManager, menuElement: String) : Sensor? {
        when (menuElement) {
            "腕立て伏せ", "上体起こし" -> return sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        }
        return null
    }

    fun judgeProximity(sensorValue: Float?, canUsed: Boolean) : Boolean {
        return sensorValue!! == 0.0f && canUsed
    }
    fun finishTimes(counter: Int, finishTimes: Int)
    fun finishTraining(setter: Int, finishSets: Int)
        //return setter == finishSets

}