package com.drekaz.muscle.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.drekaz.muscle.ui.training.TrainingSensorFragmentDirections
import com.drekaz.muscle.ui.training.TrainingGPSFragmentDirections

class DialogError(private val msg: String, private val usedSensor: Boolean) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext()).also {
            it.setTitle("エラー")
            it.setMessage(msg)
            it.setPositiveButton("OK") { _, _ ->
                if(usedSensor) {
                    val action = TrainingSensorFragmentDirections.actionTrainingMenu()
                    findNavController().navigate(action)
                } else {
                    val action = TrainingGPSFragmentDirections.actionTrainingMenu()
                    findNavController().navigate(action)
                }
            }
        }
        return builder.create()
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }
}