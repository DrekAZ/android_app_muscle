package com.drekaz.muscle.ui.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.drekaz.muscle.R

class DialogChangeBodyInfo : DialogFragment() {

    private lateinit var alertDialog: AlertDialog
    private lateinit var positiveButton: Button
    private lateinit var editHeight: EditText
    private lateinit var editWeight: EditText
    private lateinit var editFat: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_change_body_info, null)
        editHeight = view.findViewById(R.id.edit_height)
        editWeight = view.findViewById(R.id.edit_weight)
        editFat = view.findViewById(R.id.edit_fat)

        val builder = AlertDialog.Builder(requireContext()).apply {
            setView(view)
            setPositiveButton("OK") { _, _ ->
                val heightText = nullToBlank(editHeight.text)
                val weightText = nullToBlank(editWeight.text)
                val fatText = nullToBlank(editFat.text)

                val arrayStr = arrayOf(heightText, weightText, fatText)
                println(arrayStr.contentToString())
                val fragment = targetFragment
                if (fragment != null) {
                    val data = Intent().putExtra("BodyInfo", arrayStr)
                    fragment.onActivityResult(targetRequestCode, Activity.RESULT_OK, data)
                }
            }
            setNegativeButton("cancel") { _, _ -> }
        }

        alertDialog = builder.create()
        return alertDialog
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    override fun onStart() {
        super.onStart()

        positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.isEnabled = false

        editHeight.doAfterTextChanged {
            positiveButton.isEnabled = checkText()
        }
        editWeight.doAfterTextChanged {
            positiveButton.isEnabled = checkText()
        }
        editFat.doAfterTextChanged {
            positiveButton.isEnabled = checkText()
        }
    }

    private fun checkText(): Boolean {
        return !( editHeight.text.toString().isBlank() &&
                editWeight.text.toString().isBlank() &&
                editFat.text.toString().isBlank() )
    }

    private fun nullToBlank(edit: Editable): String {
        return if(edit.toString().isBlank()) "" else edit.toString()
    }
}