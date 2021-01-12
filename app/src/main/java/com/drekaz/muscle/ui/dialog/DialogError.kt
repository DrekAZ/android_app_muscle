package com.drekaz.muscle.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.drekaz.muscle.ui.training.TrainingFragmentDirections

class DialogError(private val msg: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext()).also {
            it.setTitle("エラー")
            it.setMessage(msg)
            it.setPositiveButton("OK") { _, _ ->
                val action = TrainingFragmentDirections.actionTrainingMenu()
                findNavController().navigate(action)
            }
        }
        return builder.create()
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }
}