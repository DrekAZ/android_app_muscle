package com.drekaz.muscle.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.drekaz.muscle.R
import com.drekaz.muscle.database.entity.TrainingEntity
import java.util.jar.Attributes

class WeightGraphView(context: Context, attributeSet: AttributeSet): LinearLayout(context, attributeSet) {

    init {
        View.inflate(context, R.layout.view_weight_graph, this)


    }
}