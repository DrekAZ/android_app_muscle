package com.drekaz.muscle.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
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
                    dismiss()
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

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.95).toInt()
        dialog?.window?.setLayout(width, height)
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