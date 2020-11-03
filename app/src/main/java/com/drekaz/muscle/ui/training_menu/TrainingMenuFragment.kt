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

class TrainingMenuFragment : Fragment() {
    private val menus = arrayOf(
        "腕立て伏せ",
        "プランク",
        "クランチ",
        "スクワット",
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_training_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView : ListView = view.findViewById(R.id.list_view)
        val adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, menus)
        listView.adapter = adapter
        listView.setOnItemClickListener { parent, view_list, position, id ->
            val element = parent.getItemAtPosition(position)
            val gifView = ImageView(view.context)
            //val imageViewTarget = GlideDrawableImageViewTarget(gifView)
            val builder = AlertDialog.Builder(view.context).apply {
                setTitle(element.toString())
                setMessage("HALLO")
                setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    findNavController().navigate(R.id.action_menu_training)
                })
                setNegativeButton("cancel", DialogInterface.OnClickListener {dialog, which ->
                })
                setView(gifView)
                //Glide.with(view.context).load(R.raw.pushup).into(gifView)
                Glide.with(context).load(R.raw.pushup).override(480,270).into(gifView)
            }
            builder.show()

        }
    }
}
