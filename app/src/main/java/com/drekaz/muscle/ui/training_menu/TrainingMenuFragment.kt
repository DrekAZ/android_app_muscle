package com.drekaz.muscle.ui.training_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.drekaz.muscle.R

class TrainingMenuFragment : Fragment() {
    companion object {
        const val TAG = "TrainingMenuFragment"
    }

    private val menus = arrayOf<String>(
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

        val listView : ListView = view.findViewById<ListView>(R.id.list_view)
        val adapter = ArrayAdapter<String>(view.context, android.R.layout.simple_list_item_1, menus)
        listView.adapter = adapter
    }
}
