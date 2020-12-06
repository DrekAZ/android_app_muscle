package com.drekaz.muscle.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.drekaz.muscle.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class TrainingGraphView(contexts: Context, attributeSet: AttributeSet): LinearLayout(contexts, attributeSet) {

    init {
        View.inflate(context, R.layout.view_calories_graph, this)

        val entryList = mutableListOf<Entry>()
        fun setData(x: List<Float>, y: List<Float>, ) {
            for(i in x.indices){
                entryList.add(
                    Entry(x[i], y[i])
                )
            }
            val lineDataSets = mutableListOf<ILineDataSet>()
            val lineDataSet = LineDataSet(entryList, "square")
            lineDataSet.color = Color.BLUE
            lineDataSets.add(lineDataSet)
            val lineData = LineData(lineDataSets)
            /*val lineChart = view
            lineChart.data = lineData
            //X軸の設定
            lineChart.xAxis.apply {
                isEnabled = true
                textColor = Color.BLACK
            }*/
            //lineChart.invalidate()
        }
    }

}