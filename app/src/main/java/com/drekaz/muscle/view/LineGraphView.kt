package com.drekaz.muscle.view

import android.graphics.Color
import android.view.View
import com.drekaz.muscle.R
import com.drekaz.muscle.database.entity.BodyInfoEntity
import com.drekaz.muscle.database.entity.CaloriesEntity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LineGraphView(private val view: View) {
    private lateinit var lineChart: LineChart

    fun setGraph(caloriesList: List<CaloriesEntity>, weightList: List<BodyInfoEntity>) {
        val x = dataToX(caloriesList)
        val caloriesLineDataSet = setCaloriesData(caloriesList)
        val weightLineDataSet = setWeightData(weightList)

        val lineDataSets = mutableListOf<ILineDataSet>()
        lineDataSets.add(caloriesLineDataSet)
        lineDataSets.add(weightLineDataSet)

        lineChart = view.findViewById(R.id.graph_line)
        lineChart.data = LineData(lineDataSets)
        lineChart.xAxis.apply {
            isEnabled = true
            textColor = Color.BLACK
            valueFormatter = IndexAxisValueFormatter(x)
        }
        lineChart.legend.textSize = 12f
        lineChart.description.isEnabled = false
        lineChart.axisLeft.axisMinimum = 0f
        lineChart.axisRight.axisMinimum = 0f

        lineChart.invalidate()
    }

    private fun dataToX(dataList: List<CaloriesEntity>): List<String> {
        val formatter = DateTimeFormatter.ofPattern("MM/dd")
        var today = LocalDate.now()
        val label = dataList.map { it.date.format(formatter) }.toMutableList()
        /// データが7つない場合今日の日付が2つ以上存在 -> 明日以降の日付に変更
        val index = label.indexOf( today.format(formatter) )
        for(i in index + 1 .. label.lastIndex) {
            today = today.plusDays(1)
            label[i] = today.format(formatter)
        }

        return label
    }

    private fun dataToCalories(dataList: List<CaloriesEntity>): List<Float> {
        return dataList.map { it.calories }
    }
    private fun dataToWeight(dataList: List<BodyInfoEntity>): List<Float> {
        return dataList.map { it.weight }
    }

    private fun setData(y: List<Float>): List<Entry> {
        val entryList = mutableListOf<Entry>()
        for(i in y.indices) {
            entryList.add(Entry(i.toFloat(), y[i]))
        }

        return entryList
    }

    private fun setCaloriesData(dataList: List<CaloriesEntity>): LineDataSet {
        val y = dataToCalories(dataList)
        val entryList = setData(y)

        val lineDataSet = LineDataSet(entryList, "消費カロリー")
        lineDataSet.color = Color.parseColor("#F57C00")
        lineDataSet.valueTextSize = 13f

        return lineDataSet
    }
    private fun setWeightData(dataList: List<BodyInfoEntity>): LineDataSet {
        val y = dataToWeight(dataList)
        val entryList = setData(y)

        val lineDataSet = LineDataSet(entryList, "体重")
        lineDataSet.color = Color.parseColor("#03A9F4")
        lineDataSet.valueTextSize = 13f

        return lineDataSet
    }
}