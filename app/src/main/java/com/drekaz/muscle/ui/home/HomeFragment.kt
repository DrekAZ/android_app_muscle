package com.drekaz.muscle.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.drekaz.muscle.R
import com.github.mikephil.charting.charts.LineChart

class HomeFragment : Fragment() {
    companion object {
        private const val TAG = "HomeFragment"
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val normalText : TextView = view.findViewById(R.id.normal_mode)
        normalText.setOnClickListener{
            findNavController().navigate(R.id.action_home_menu)
        }
        val coopText : TextView = view.findViewById(R.id.coop_mode)
        coopText.setOnClickListener{
        }
    }
}