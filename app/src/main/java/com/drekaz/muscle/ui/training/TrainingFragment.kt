package com.drekaz.muscle.ui.training

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.drekaz.muscle.R
import com.drekaz.muscle.databinding.FragmentTrainingBinding
import com.drekaz.muscle.training_ViewModel.CounterViewModel
import com.drekaz.muscle.ui.dialog.DialogTrainingDesc
import com.drekaz.muscle.ui.dialog.DialogTrainingSelect
import kotlin.math.floor

class TrainingFragment : Fragment(), SensorEventListener {
    private lateinit var menuElement: String
    private lateinit var pickerArray: IntArray
    private val counterViewModel: CounterViewModel by viewModels()
    private lateinit var sensorManager: SensorManager
    private var proximity: Sensor? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTrainingBinding.inflate(inflater, container, false)
        binding.vm= counterViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuElement = TrainingFragmentArgs.fromBundle(arguments ?: return).menuElement
        val selectDialog = DialogTrainingSelect().apply {
            setTargetFragment(this@TrainingFragment, 200)
            arguments = Bundle()
        }
        selectDialog.show(parentFragmentManager, null)

        sensorManager = view.context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        if( !sensorManager.getSensorList(Sensor.TYPE_PROXIMITY).isNullOrEmpty() ) {
            // 最大sensorDelayの時間まで待たれる
            sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            200 -> if(resultCode == Activity.RESULT_OK) {
                pickerArray = data?.getIntArrayExtra("picker")!!
                val descDialog = DialogTrainingDesc(menuElement).apply {
                    setTargetFragment(this@TrainingFragment, 201)
                }
                descDialog.show(parentFragmentManager, null)
            }
            201 -> if(resultCode == Activity.RESULT_OK) {
                println("201 OK")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        proximity?.also {
            sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        val value = event?.values?.get(0)
        if (value!! == 0.0f) {
            counterViewModel.countUp()
            if(counterViewModel.counter.value == pickerArray[1]) {
                if(counterViewModel.setNum.value == pickerArray[0] && counterViewModel.counter.value == pickerArray[1]) {
                    // intent
                    sensorManager.unregisterListener(this)
                } else {
                    counterViewModel.resetCounter()
                    counterViewModel.countUpSet()
                    restTimer(requireView().findViewById(R.id.time))
                }
            }
        }
    }

    private fun restTimer(timerText: TextView) {
        val msec = (pickerArray[2] * 1000).toLong()

        sensorManager.unregisterListener(this)
        counterViewModel.changeRestState()
        counterViewModel.setRestTimeMax(pickerArray[2])
        val timer = object: CountDownTimer(msec, 100L) {
            override fun onTick(millisUntilFinished: Long) {
                val time = floor(millisUntilFinished/1000.0).toInt()
                counterViewModel.setRestTime(pickerArray[2]-time)
                //println(pickerArray[2] - time)
            }
            override fun onFinish() {
                finishTimer()
            }
        }.start()

    }

    fun finishTimer() {
        sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
        counterViewModel.changeRestState()
    }

}