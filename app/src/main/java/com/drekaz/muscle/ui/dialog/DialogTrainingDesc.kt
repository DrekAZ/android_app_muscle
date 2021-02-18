package com.drekaz.muscle.ui.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.drekaz.muscle.R

class DialogTrainingDesc(private val menu: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val gifView = ImageView(requireContext())
        val descItems = selectDesc(menu)
        val checkedItems = BooleanArray(descItems.size) { false }
        val builder = AlertDialog.Builder(requireContext()).apply {
            isCancelable = false
            setView(gifView)
            setMultiChoiceItems(selectDesc(menu), checkedItems) { _, which, isChecked ->
                checkedItems[which] = isChecked
                if(checkedItems.all { it }) {
                    val fragment = targetFragment
                    if(fragment != null) {
                        val data = Intent().putExtra("finished", true)
                        fragment.onActivityResult(targetRequestCode, Activity.RESULT_OK, data)
                    }
                    dismiss()
                }
            }
        }
        return builder.create()
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.95).toInt()
        dialog?.window?.setLayout(width, height)
    }

    private fun selectDesc(element: String): Array<String> {
        when (element) {
            "腕立て伏せ" -> return resources.getStringArray(R.array.howto_pushup)
            "上体起こし"  -> return resources.getStringArray(R.array.howto_situp)
            "ウォーキング・ランニング" -> return resources.getStringArray(R.array.howto_run)
            "クランチ"   -> return resources.getStringArray(R.array.howto_crunch)
        }
        return resources.getStringArray(R.array.howto_squat)
    }
}