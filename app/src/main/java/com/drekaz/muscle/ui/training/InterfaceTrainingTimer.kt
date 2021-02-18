package com.drekaz.muscle.ui.training

import java.time.LocalDateTime
import java.time.ZoneOffset

interface InterfaceTrainingTimer {
    var trainingStartTime: Long

    fun startTrainingTimer() : Long {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
    }
    fun stopTrainingTimer(startTime: Long, sets: Int, restSec: Int) : Float {
        val stopTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        val restTime = (sets - 1) * restSec
        /// Hour
        return (stopTime - startTime - restTime) / 3600f
    }
}