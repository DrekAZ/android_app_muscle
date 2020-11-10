package com.drekaz.muscle.ui.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.drekaz.muscle.R
import com.drekaz.muscle.ui.training.TrainingFragment

class DialogTrainingDesc(private val menu: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val gifView = ImageView(requireContext())
        val descItems = selectDesc(menu)
        val checkedItems = BooleanArray(descItems.size) { false }
        val builder = AlertDialog.Builder(requireContext()).apply {
            //setTitle("やり方")
            isCancelable = false
            setView(gifView)
            setMultiChoiceItems(selectDesc(menu), checkedItems) { dialog, which, isChecked ->
                checkedItems[which] = isChecked
                if(checkedItems.all { it }) {
                    dismiss()
                    //targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, null)
                }
            }
            Glide.with(context).load(R.raw.pushup).override(960,540).into(gifView)
        }
        return builder.create()
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    private fun selectDesc(element: String): Array<String> {
        when (element) {
            "腕立て伏せ" -> return resources.getStringArray(R.array.desc_pushup)
            "プランク"   -> return resources.getStringArray(R.array.desc_plank)
            "クランチ"   -> return resources.getStringArray(R.array.desc_crunch)
        }
        return resources.getStringArray(R.array.desc_squat) // スクワット
    }
}