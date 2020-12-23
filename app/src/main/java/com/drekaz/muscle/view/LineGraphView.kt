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
        val x = dataToX(caloriesList, weightList)
        val caloriesLineDataSet = setCaloriesData(caloriesList, x)
        val weightLineDataSet = setWeightData(weightList, x)

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
        lineChart.setTouchEnabled(false)

        lineChart.invalidate()
    }

    private fun dataToX(caloriesList: List<CaloriesEntity>, weightList: List<BodyInfoEntity>): List<String> {
        val formatter = DateTimeFormatter.ofPattern("MM/dd")
        var today = LocalDate.now()
        val x =
            if(caloriesList[0].date < weightList[0].date) caloriesList.map { it.date.format(formatter) }.toMutableList()
            else weightList.map { it.date.format(formatter) }.toMutableList()
        /// データが7つない場合今日の日付が2つ以上存在 -> 明日以降の日付に変更
        val index = x.indexOf( today.format(formatter) )
        for(i in index + 1 .. x.lastIndex) {
            today = today.plusDays(1)
            x[i] = today.format(formatter)
        }

        return x
    }

    private fun dataToCalories(dataList: List<CaloriesEntity>, x: List<String>): List<Float> {
        val formatter = DateTimeFormatter.ofPattern("MM/dd")
        val list = mutableListOf<Float>()
        var i = 0
        x.forEach {
            if(dataList[i].date.format(formatter) == it) {
                list.add(dataList[i].calories)
                i ++
            } else {
                list.add(0f)
            }
        }

        return list.toList()
    }
    private fun dataToWeight(dataList: List<BodyInfoEntity>, x: List<String>): List<Float> {
        val formatter = DateTimeFormatter.ofPattern("MM/dd")
        val list = mutableListOf<Float>()
        println(x)
        var i = 0
        x.forEach {
            if(dataList[i].date.format(formatter) == it) {
                list.add(dataList[i].weight)
                i ++
            } else {
                list.add(0f)
            }
        }

        return list.toList()
    }

    private fun setData(y: List<Float>): List<Entry> {
        val entryList = mutableListOf<Entry>()
        for(i in y.indices) {
            entryList.add(Entry(i.toFloat(), y[i]))
        }

        return entryList
    }

    private fun setCaloriesData(dataList: List<CaloriesEntity>, x: List<String>): LineDataSet {
        val y = dataToCalories(dataList, x)
        val entryList = setData(y)

        val lineDataSet = LineDataSet(entryList, "消費カロリー (kcal)")
        lineDataSet.color = Color.parseColor("#F57C00")
        lineDataSet.valueTextSize = 13f

        return lineDataSet
    }
    private fun setWeightData(dataList: List<BodyInfoEntity>, x: List<String>): LineDataSet {
        val y = dataToWeight(dataList, x)
        val entryList = setData(y)

        val lineDataSet = LineDataSet(entryList, "体重 (kg)")
        lineDataSet.color = Color.parseColor("#03A9F4")
        lineDataSet.valueTextSize = 13f

        return lineDataSet
    }
}