package com.drekaz.muscle.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.drekaz.muscle.R
import com.drekaz.muscle.ui.training_menu.TrainingMenuFragmentDirections

class DialogTrainingMenu(private val menu: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val gifView = ImageView(requireContext())
        val builder = AlertDialog.Builder(requireContext()).apply {
            setTitle(menu)
            setView(gifView)
            selectDesc(context, menu, gifView)
            setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                val action = TrainingMenuFragmentDirections.actionMenuTraining(menuElement = menu)
                findNavController().navigate(action)
            })
            setNegativeButton("cancel", DialogInterface.OnClickListener { _, _ ->
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

    private fun selectDesc(context: Context, element: String, gifView: ImageView) {
        when (element) {
            "腕立て伏せ" -> Glide.with(context).load(R.raw.pushup).override(480,270).into(gifView)
            "プランク"   -> Glide.with(context).load(R.raw.pushup).override(480,270).into(gifView)
            "クランチ"   -> Glide.with(context).load(R.raw.pushup).override(480,270).into(gifView)
            "スクワット" -> Glide.with(context).load(R.raw.pushup).override(480,270).into(gifView)
        }
    }
}