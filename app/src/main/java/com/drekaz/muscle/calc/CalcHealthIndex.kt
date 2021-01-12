package com.drekaz.muscle.calc

import kotlin.math.roundToInt

class CalcHealthIndex {
    companion object {
        private val metsMap = mapOf(
            "腕立て伏せ" to 3.8f,
            "上体起こし" to 2.8f,
            "プランク" to 2.8f,
            "スクワット" to 5.0f
        )
        private const val calorieConst = 1.05f
        private const val manNearLBMConst1 = 0.32810f
        private const val manNearLBMConst2 = 0.33929f
        private const val manNearLBMConst3 = 29.5336f
        private const val womanNearLBMConst1 = 0.29569f
        private const val womanNearLBMConst2 = 0.41813f
        private const val womanNearLBMConst3 = 43.2933f
    }

    fun calcCalorie(menu: String, kg: Float, hour: Float):
            Float =
        if (metsMap[menu] != null) kg * hour * metsMap[menu]!! * calorieConst
        else 0.0f

    fun calcBMI(kg: Float, m: Float):
            Float = (kg / (m * m) * 10).roundToInt() / 10.0f

    /// 除脂肪体重: Lean body mass (kg)
    fun calcLBM(kg: Float, fatPercnet: Float):
            Float = (kg * (100-fatPercnet) / 100 * 10).roundToInt() / 10.0f

    /// 近似値
    fun calcNearLBM(kg: Float, cm: Float, isMan: Boolean):
            Float =
        if (isMan) ((manNearLBMConst1 * kg + manNearLBMConst2 * cm - manNearLBMConst3) * 10).roundToInt() / 10.0f
        else ((womanNearLBMConst1 * kg + womanNearLBMConst2 * cm - womanNearLBMConst3) * 10).roundToInt() / 10.0f

    /// 筋肉指数
    fun calcLBMI(LBM: Float, m: Float):
            Float = (LBM / m / m * 10).roundToInt() / 10.0f
}