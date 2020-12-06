package com.drekaz.muscle.database

import com.drekaz.muscle.database.entity.CaloriesEntity
import com.drekaz.muscle.database.entity.TrainingEntity
import java.time.LocalDate

class InitEntity {
    /// avoid null and init entity
    val caloriesTestData = CaloriesEntity(0,0.0f, LocalDate.now())
    val trainingTestData = TrainingEntity(0, "", 0,0, LocalDate.now(),0)
}