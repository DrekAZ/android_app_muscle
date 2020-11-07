package com.drekaz.muscle.ui.training_menu

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.media.Image
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

import com.drekaz.muscle.R
import com.drekaz.muscle.ui.dialog.DialogTrainingMenu

class TrainingMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_training_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menus = resources.getStringArray(R.array.menus)
        val listView : ListView = view.findViewById(R.id.list_view)
        val adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, menus)
        listView.adapter = adapter
        listView.setOnItemClickListener { parent, view_list, position, id ->
            val element = parent.getItemAtPosition(position)
            val dialog = DialogTrainingMenu(element.toString())
            dialog.show(parentFragmentManager, null)
        }
    }
}
