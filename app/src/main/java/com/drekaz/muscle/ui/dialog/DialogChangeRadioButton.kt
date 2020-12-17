package com.drekaz.muscle.ui.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class DialogChangeRadioButton(private val num: Int): DialogFragment() {

    private val items = arrayOf("男性", "女性", "その他")
    private var selectNum = if(num == 9) 2 else num - 1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext()).apply {
            setSingleChoiceItems(items, selectNum) { _, which ->
                selectNum = which
            }
            setPositiveButton("OK") { _, _ ->
                val fragment = targetFragment
                if(fragment != null) {
                    selectNum = if(selectNum == 2) 9 else selectNum + 1
                    val data = Intent().putExtra("radio", selectNum)
                    fragment.onActivityResult(targetRequestCode, Activity.RESULT_OK, data)
                }
            }
            setNegativeButton("cancel") { _, _ -> }
        }

        return builder.create()
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }
}