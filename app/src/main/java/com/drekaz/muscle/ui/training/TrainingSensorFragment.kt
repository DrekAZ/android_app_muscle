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
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.drekaz.muscle.database.BodyInfoDatabase
import com.drekaz.muscle.database.CaloriesDatabase
import com.drekaz.muscle.database.TrainingDatabase
import com.drekaz.muscle.databinding.FragmentSensorTrainingBinding
import com.drekaz.muscle.ui.dialog.DialogError
import com.drekaz.muscle.ui.dialog.DialogTrainingDesc
import com.drekaz.muscle.ui.dialog.DialogTrainingResult
import com.drekaz.muscle.ui.dialog.DialogTrainingSelect
import kotlinx.coroutines.runBlocking
import kotlin.math.floor

class TrainingSensorFragment : Fragment(), SensorEventListener, InterfaceSensorProcess, InterfaceTrainingTimer {
    private val trainingViewModel: TrainingViewModel by viewModels()
    private lateinit var bodyInfoDatabase: BodyInfoDatabase
    private lateinit var trainingDatabase: TrainingDatabase
    private lateinit var caloriesDatabase: CaloriesDatabase

    private lateinit var menuElement: String
    private lateinit var pickerArray: IntArray

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private var canSensing = false
    private var descFinished = false

    override var trainingStartTime: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bodyInfoDatabase = BodyInfoDatabase.getInstance(requireContext())
        trainingDatabase = TrainingDatabase.getInstance(requireContext())
        caloriesDatabase = CaloriesDatabase.getInstance(requireContext())

        val binding = FragmentSensorTrainingBinding.inflate(inflater, container, false)
        binding.vm= trainingViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runBlocking {
            trainingViewModel.readBodyInfoData(bodyInfoDatabase)
        }

        menuElement = TrainingSensorFragmentArgs.fromBundle(arguments ?: return).menuElement

        sensorManager = view.context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = getSensor(sensorManager, menuElement)
        if(sensor == null) {
            showDialogFragment(DialogError("スマートフォンにセンサーがないためこのトレーニングはできません", true), null, null)
        } else {
            // Intent Dialog Select
            showDialogFragment(DialogTrainingSelect(), Bundle(), 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            1 -> if(resultCode == Activity.RESULT_OK) {
                pickerArray = data?.getIntArrayExtra("picker")!!
                showDialogFragment(DialogTrainingDesc(menuElement), null, 2)
            }
            2 -> if(resultCode == Activity.RESULT_OK) {
                descFinished = true

                if( !sensorManager.getSensorList(Sensor.TYPE_PROXIMITY).isNullOrEmpty() ) {
                    enabledSensor(true)
                }

                // start timer
                trainingStartTime = startTrainingTimer()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(descFinished) {
            enabledSensor(true)
        }
    }

    override fun onPause() {
        super.onPause()
        enabledSensor(false)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        val value = event?.values?.get(0)
        if(countJudge(value, canSensing, menuElement)) {
            trainingViewModel.countUp()

            finishTimes(trainingViewModel.counter.value!!, pickerArray[1])
        }
    }

    override fun finishTimes(counter: Int, finishTimes: Int) {
        // finish times
        if(counter == finishTimes) {
            trainingViewModel.countUpSet()
            finishTraining(trainingViewModel.setNum.value!!, pickerArray[0])
        }
    }

    override fun finishTraining(setter: Int, finishSets: Int) {
        // finish training
        if(setter == finishSets) {
            // END
            val trainingHour = stopTrainingTimer(trainingStartTime, pickerArray[0], pickerArray[2])

            enabledSensor(false)
            trainingViewModel.saveData(menuElement, trainingDatabase, caloriesDatabase, trainingHour)
            // Intent Dialog Result
            showDialogFragment(DialogTrainingResult(menuElement, trainingViewModel.counter.value!!, trainingViewModel.setNum.value!!, trainingViewModel.myBodyInfo.value!!.weight, trainingHour), null, null)
        } else {
            trainingViewModel.resetCounter()
            restTimer()
        }
    }

    private fun enabledSensor(isEnabled: Boolean) {
        // 最大sensorDelayの時間まで待たれる
        if(isEnabled) {
            canSensing = true
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        } else {
            canSensing = false
            sensorManager.unregisterListener(this)
        }
    }

    private fun showDialogFragment(dialogFragment: DialogFragment, bundle: Bundle?, requestCode: Int?) {
        dialogFragment.also {
            if(requestCode != null) it.setTargetFragment(this@TrainingSensorFragment, requestCode)
            if(bundle != null) it.arguments = bundle
        }
        dialogFragment.show(parentFragmentManager, null)
    }

    private fun restTimer() {
        val mSec = (pickerArray[2] * 1000).toLong()

        enabledSensor(false)
        trainingViewModel.changeRestState()
        trainingViewModel.setRestTimeMax(pickerArray[2])
        object: CountDownTimer(mSec, 100L) {
            override fun onTick(millisUntilFinished: Long) {
                val time = floor(millisUntilFinished/1000.0).toInt()
                trainingViewModel.setRestTime(pickerArray[2] - time)
            }
            override fun onFinish() {
                finishRestTimer()
            }
        }.start()
    }

    fun finishRestTimer() {
        enabledSensor(true)
        trainingViewModel.changeRestState()
    }
}