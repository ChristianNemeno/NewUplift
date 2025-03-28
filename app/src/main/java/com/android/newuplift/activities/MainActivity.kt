package com.android.newuplift.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.android.newuplift.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
            ?.findNavController() // Use safe call to avoid potential null pointer exception

        if (navController != null) {
            bottomNavigationView.setupWithNavController(navController)

            // Handle navigation item selection to clear the stack
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.settingsFragment -> {
                        navController.popBackStack(R.id.settingsFragment, true) // Clear stack up to settings
                        navController.navigate(R.id.settingsFragment) // Navigate to settings
                        true
                    }
                    R.id.homeFragment -> {
                        navController.popBackStack(R.id.homeFragment, true)
                        navController.navigate(R.id.homeFragment)
                        true
                    }
                    R.id.profileFragment -> {
                        navController.popBackStack(R.id.profileFragment, true)
                        navController.navigate(R.id.profileFragment)
                        true
                    }
                    else -> false
                }
            }
        }

    }

}