package com.drekaz.muscle.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.drekaz.muscle.database.BodyInfoDatabase
import com.drekaz.muscle.database.CaloriesDatabase
import com.drekaz.muscle.database.UserDatabase
import com.drekaz.muscle.databinding.FragmentDashboardBinding
import com.drekaz.muscle.view.LineGraphView

class DashboardFragment : Fragment() {
    companion object {
        private lateinit var userDatabase: UserDatabase
        private lateinit var caloriesDatabase: CaloriesDatabase
        private lateinit var bodyInfoDatabase: BodyInfoDatabase
        private lateinit var dashboardViewModel: DashboardViewModel
        private lateinit var binding: FragmentDashboardBinding
        private lateinit var lineGraph: LineGraphView
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel = ViewModelProvider.NewInstanceFactory().create(DashboardViewModel::class.java)
        userDatabase = UserDatabase.getInstance(requireContext())
        caloriesDatabase = CaloriesDatabase.getInstance(requireContext())
        bodyInfoDatabase = BodyInfoDatabase.getInstance(requireContext())

        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = dashboardViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashboardViewModel.readUserData(userDatabase)
        dashboardViewModel.readDayBodyInfo(bodyInfoDatabase)
        dashboardViewModel.readWeekBodyInfo(bodyInfoDatabase)
        dashboardViewModel.readWeekCalories(caloriesDatabase)
        dashboardViewModel.calcBmi()

        lineGraph = LineGraphView(view)
        lineGraph.setGraph(dashboardViewModel.weekCalories.value!!, dashboardViewModel.weekBodyInfo.value!!)
    }
}