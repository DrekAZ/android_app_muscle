package com.drekaz.muscle.ui.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment

class DialogChangeText(private val text: String): DialogFragment() {
    private lateinit var alertDialog: AlertDialog
    private lateinit var positiveButton: Button
    private lateinit var editText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        editText = EditText(requireContext())
        editText.setText(text)
        val builder = AlertDialog.Builder(requireContext()).apply {
            setView(editText)
            setPositiveButton("OK") { _, _ ->
                val fragment = targetFragment
                if(fragment != null) {
                    val data = Intent().putExtra("text", editText.text.toString())
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
        editText.doAfterTextChanged {
            positiveButton.isEnabled = checkText()
        }
    }

    private fun checkText(): Boolean {
        return !( editText.text.toString().isBlank() )
    }
}