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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.drekaz.muscle.database.BodyInfoDatabase
import com.drekaz.muscle.database.TrainingDatabase
import com.drekaz.muscle.database.UserDatabase
import com.drekaz.muscle.database.entity.BodyInfoEntity
import com.drekaz.muscle.database.entity.UserEntity
import com.drekaz.muscle.databinding.FragmentTrainingBinding
import com.drekaz.muscle.ui.dialog.DialogTrainingDesc
import com.drekaz.muscle.ui.dialog.DialogTrainingResult
import com.drekaz.muscle.ui.dialog.DialogTrainingSelect
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.floor

class TrainingFragment : Fragment(), SensorEventListener {
    private lateinit var menuElement: String
    private lateinit var pickerArray: IntArray
    private val trainingViewModel: TrainingViewModel by viewModels()
    private lateinit var sensorManager: SensorManager
    private var proximity: Sensor? = null
    private lateinit var bodyInfoDatabase: BodyInfoDatabase
    private lateinit var trainingDatabase: TrainingDatabase
    private var canSensing = false
    private var bodyInfoData: BodyInfoEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bodyInfoDatabase = BodyInfoDatabase.getInstance(requireContext())
        trainingDatabase = TrainingDatabase.getInstance(requireContext())

        val binding = FragmentTrainingBinding.inflate(inflater, container, false)
        binding.vm= trainingViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runBlocking {
            GlobalScope.launch {
                bodyInfoData = trainingViewModel.readBodyInfoData(bodyInfoDatabase)
            }.join()
        }

        menuElement = TrainingFragmentArgs.fromBundle(arguments ?: return).menuElement
        val selectDialog = DialogTrainingSelect().apply {
            setTargetFragment(this@TrainingFragment, 200)
            arguments = Bundle()
        }
        // Intent Dialog Select
        selectDialog.show(parentFragmentManager, null)

        sensorManager = view.context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        /*if( !sensorManager.getSensorList(Sensor.TYPE_PROXIMITY).isNullOrEmpty() ) {
            // 最大sensorDelayの時間まで待たれる
            sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
        }*/
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
                canSensing = true
                if( !sensorManager.getSensorList(Sensor.TYPE_PROXIMITY).isNullOrEmpty() ) {
                    // 最大sensorDelayの時間まで待たれる
                    sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        canSensing = true
        proximity?.also {
            sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        canSensing = false
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        val value = event?.values?.get(0)
        if (value!! == 0.0f && canSensing) {
            trainingViewModel.countUp()
            if(trainingViewModel.counter.value == pickerArray[1]) {
                if(trainingViewModel.setNum.value == pickerArray[0] && trainingViewModel.counter.value == pickerArray[1]) {
                    // END
                    sensorManager.unregisterListener(this)
                    trainingViewModel.saveData(menuElement, trainingDatabase)
                    // Intent Dialog Result
                    val resultDialog = DialogTrainingResult(menuElement, trainingViewModel.counter.value!!, trainingViewModel.setNum.value!!, bodyInfoData!!.weight, bodyInfoData!!.fat)
                    resultDialog.show(parentFragmentManager, null)
                } else {
                    trainingViewModel.resetCounter()
                    trainingViewModel.countUpSet()
                    restTimer()
                }
            }
        }
    }

    private fun restTimer() {
        val mSec = (pickerArray[2] * 1000).toLong()

        sensorManager.unregisterListener(this)
        trainingViewModel.changeRestState()
        trainingViewModel.setRestTimeMax(pickerArray[2])
        object: CountDownTimer(mSec, 100L) {
            override fun onTick(millisUntilFinished: Long) {
                val time = floor(millisUntilFinished/1000.0).toInt()
                trainingViewModel.setRestTime(pickerArray[2]-time)
            }
            override fun onFinish() {
                finishTimer()
            }
        }.start()

    }

    fun finishTimer() {
        sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
        trainingViewModel.changeRestState()
    }

}