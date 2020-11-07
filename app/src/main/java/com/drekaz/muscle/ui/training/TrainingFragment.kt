package com.drekaz.muscle.ui.training

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.drekaz.muscle.R
import com.drekaz.muscle.ui.dialog.DialogTrainingDesc

class TrainingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_training, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuElement = TrainingFragmentArgs.fromBundle(arguments ?: return).menuElement
        val dialog = DialogTrainingDesc(menuElement)
        dialog.show(parentFragmentManager, null)
    }

}