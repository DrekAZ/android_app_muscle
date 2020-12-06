package com.drekaz.muscle.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.drekaz.muscle.R
import com.drekaz.muscle.database.CaloriesDatabase
import com.drekaz.muscle.database.UserDatabase
import com.drekaz.muscle.databinding.ActivityFirstLaunchBinding
import com.drekaz.muscle.databinding.FragmentDashboardBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DashboardFragment : Fragment() {

    private lateinit var userDatabase: UserDatabase
    private lateinit var caloriesDatabase: CaloriesDatabase
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel = ViewModelProvider.NewInstanceFactory().create(DashboardViewModel::class.java)
        userDatabase = UserDatabase.getInstance(requireContext())
        caloriesDatabase = CaloriesDatabase.getInstance(requireContext())

        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = dashboardViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashboardViewModel.readUserData(userDatabase)
        dashboardViewModel.readDayCalories(caloriesDatabase)
        dashboardViewModel.calcBmi()


    }

}