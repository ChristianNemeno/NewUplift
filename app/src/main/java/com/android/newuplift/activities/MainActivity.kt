package com.android.newuplift.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.android.newuplift.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    // Inside MainActivity class
    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        // Check if the UI mode (dark/light theme) has changed
        // You might need to store the previous configuration's uiMode to compare
        // For simplicity, we often just recreate on any uiMode change handled here.
        // A more robust check compares newConfig.uiMode with the previous one.

        // Basic check: If the system is now dark, and your app wasn't, or vice versa.
        // Or, simply recreate whenever onConfigurationChanged is called for uiMode.
//        Log.d("MainActivity", "onConfigurationChanged triggered by UI mode change.")
//        recreate() // Manually trigger recreation to apply theme changes
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_SELECTED
        val navController = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
            ?.findNavController()

        if (navController != null) {
            bottomNavigationView.setupWithNavController(navController)

            bottomNavigationView.setOnItemSelectedListener { item ->
                Log.d("MainActivity", "Selected item: ${item.itemId}")
                val currentDestination = navController.currentDestination?.id
                if (currentDestination == item.itemId) {
                    Log.d("MainActivity", "Already on destination: ${item.itemId}")
                    true
                } else {
                    when (item.itemId) {
                        R.id.settingsFragment -> {
                            if (!navController.popBackStack(R.id.settingsFragment, false)) {
                                navController.navigate(R.id.settingsFragment)
                            }
                            true
                        }
                        R.id.homeFragment -> {
                            if (!navController.popBackStack(R.id.homeFragment, false)) {
                                navController.navigate(R.id.homeFragment)
                            }
                            true
                        }
                        R.id.favoriteQuotesFragment -> {
                            if (!navController.popBackStack(R.id.favoriteQuotesFragment, false)) {
                                navController.navigate(R.id.favoriteQuotesFragment)
                            }
                            true
                        }
                        R.id.myQuotes -> {
                            if (!navController.popBackStack(R.id.myQuotes, false)) {
                                navController.navigate(R.id.myQuotes)
                            }
                            true
                        }
                        else -> false
                    }
                }
            }
        }
    }
}