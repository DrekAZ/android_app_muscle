package com.drekaz.muscle.ui.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.drekaz.muscle.R
import kotlinx.android.synthetic.main.dialog_number_picker.view.*

class DialogTrainingSelect : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val pickerArray = intArrayOf(1,1,10)
        val pickerLayout = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_number_picker, null)
        pickerLayout.sets_picker.apply {
            minValue = 1
            maxValue = 10
            setOnValueChangedListener { _, _, newVal ->
                pickerArray[0] = newVal
            }
        }
        pickerLayout.times_picker.apply {
            minValue = 1
            maxValue = 100
            setOnValueChangedListener { _, _, newVal ->
                pickerArray[1] = newVal
            }
        }
        pickerLayout.rest_picker.apply {
            minValue = 10
            maxValue = 90
            setOnValueChangedListener { _, _, newVal ->
                pickerArray[2] = newVal
            }
        }

        val builder = AlertDialog.Builder(requireContext()).apply {
            isCancelable = false
            setView(pickerLayout)
            setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                val fragment = targetFragment
                if(fragment != null) {
                    val data = Intent().putExtra("picker", pickerArray)
                    fragment.onActivityResult(targetRequestCode, Activity.RESULT_OK, data)
                }
            })
        }
        return builder.create()
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    override fun onStart() {
        super.onStart()
        /*val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.95).toInt()
        dialog?.window?.setLayout(width, height)*/
    }
}