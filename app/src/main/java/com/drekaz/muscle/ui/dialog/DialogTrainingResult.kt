package com.drekaz.muscle.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.drekaz.muscle.R
import com.drekaz.muscle.calc.CalcHealthIndex

class DialogTrainingResult(private val menu: String, private val counter: Int, private val setNum: Int, private val kg: Float, private val hour: Float) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calcData = CalcHealthIndex()
        val message = """
            ${setNum}セット
            ${counter}回
            
            達成
            
            消費カロリー  約 ${calcData.calcCalorie(menu, kg, hour)}
            """
        val builder = AlertDialog.Builder(requireContext()).apply {
            isCancelable = false
            setTitle("結果")
            setMessage(message)
            setPositiveButton("トレーニング画面に戻る") { _, _ ->
                findNavController().navigate(R.id.action_training_menu)
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
        /*val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.95).toInt()
        dialog?.window?.setLayout(width, height)*/
    }
}
