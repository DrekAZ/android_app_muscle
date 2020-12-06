package com.drekaz.muscle.view

import android.content.Context
import android.graphics.Color
import android.renderscript.ScriptGroup
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.drekaz.muscle.R
import com.drekaz.muscle.database.entity.CaloriesEntity
import com.drekaz.muscle.databinding.FragmentDashboardBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet

class CaloriesGraphView (context: Context, attributeSet: AttributeSet? = null): LinearLayout(context, attributeSet) {

    private val entryList = mutableListOf<BarEntry>()
    val binding: FragmentDashboardBinding =
        FragmentDashboardBinding.inflate(LayoutInflater.from(context),this,true).apply {
            lifecycleOwner = context as? LifecycleOwner
        }

    init {

        if(binding.vm?.dayCalories?.value != null){
            val dataList = listOf(binding.vm!!.dayCalories.value!!)
            val x = dataToX(dataList)
            val y = dataToY(dataList)
            setData(y)
            val barDataSet = BarDataSet(entryList, "消費カロリー")
            barDataSet.color = Color.parseColor("#F57C00")

            val barDataSets = mutableListOf<IBarDataSet>()
            barDataSets.add(barDataSet)

            val barChart = findViewById<BarChart>(R.id.used_calories)
            barChart.data = BarData(barDataSet)
            barChart.xAxis.apply {
                isEnabled = true
                textColor = Color.BLACK
            }
            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(x)
            barChart.invalidate()
        }
    }

    private fun dataToX(dataList: List<CaloriesEntity>): List<String> {
        return dataList.map { it.date.toString() }
    }

    private fun dataToY(dataList: List<CaloriesEntity>): List<Float> {
        return dataList.map { it.calories }
    }

    private fun setData(y: List<Float>) {
        for(i in y.indices) {
            entryList.add(BarEntry(i.toFloat(), y[i]))
        }
    }
}
