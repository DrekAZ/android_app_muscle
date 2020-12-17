package com.drekaz.muscle

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.drekaz.muscle.database.UserDatabase
import com.drekaz.muscle.database.entity.UserEntity
import com.drekaz.muscle.ui.first_launch.FirstLaunchActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = UserDatabase.getInstance(this)
        var myData: UserEntity? = null
        runBlocking {
            GlobalScope.launch {
                myData = viewModel.readMyData(database)
            }.join()
        }

        if (myData == null) {
            val intent = Intent(this, FirstLaunchActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        } else {
            val navView: BottomNavigationView = findViewById(R.id.nav_view)

            val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!
                .findNavController()
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.fragment_training_menu, R.id.navigation_dashboard, R.id.navigation_setting
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)

            setFullscreen(navController, navView)
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    private fun setFullscreen(navController: NavController, navView: BottomNavigationView) {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if(destination.id == R.id.fragment_training) supportActionBar?.hide()
            navView.visibility = if(destination.id == R.id.fragment_training) View.GONE else View.VISIBLE
        }
    }
}