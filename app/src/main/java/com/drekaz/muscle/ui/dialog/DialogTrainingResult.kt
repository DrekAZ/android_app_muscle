package com.drekaz.muscle.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.drekaz.muscle.R
import com.drekaz.muscle.calc.CalcData
import com.drekaz.muscle.database.entity.UserEntity
import com.drekaz.muscle.ui.training.TrainingFragmentDirections
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DialogTrainingResult(private val menu: String, private val counter: Int, private val setNum: Int, private val kg: Float, private val hour: Float) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calcData = CalcData()
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
            setPositiveButton("トレーニング画面に戻る", DialogInterface.OnClickListener { dialog, which ->
                findNavController().navigate(R.id.action_training_menu)
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
