package com.drekaz.muscle.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
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
            setMessage( setDescMessage(menu) )
            setView(gifView)
            setDescGif(context, menu, gifView)
            setPositiveButton("OK") { _, _ ->
                var action = TrainingMenuFragmentDirections.actionMenuSensor(menuElement = menu)
                if(menu == "ウォーキング・ランニング") {
                    action = TrainingMenuFragmentDirections.actionMenuGps(menuElement = menu)
                }
                findNavController().navigate(action)
            }
            setNegativeButton("cancel") { _, _, -> }
        }
        return builder.create()
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    private fun setDescMessage(element: String) : String {
        when (element) {
            "腕立て伏せ" -> return getString(R.string.desc_pushup)
            "上体起こし" -> return getString(R.string.desc_situp)
            "ウォーキング・ランニング" -> return getString(R.string.desc_run)
        }
        return ""
    }

    private fun setDescGif(context: Context, element: String, gifView: ImageView) {
        when (element) {
            "腕立て伏せ" -> Glide.with(context).load(R.raw.push_up).override(480,270).into(gifView)
            "上体起こし" -> Glide.with(context).load(R.raw.sit_up).override(480,270).into(gifView)
            "クランチ"   -> Glide.with(context).load(R.raw.pushup).override(480,270).into(gifView)
            "スクワット" -> Glide.with(context).load(R.raw.pushup).override(480,270).into(gifView)
        }
    }
}