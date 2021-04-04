package com.andrew.photoweatherapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.andrew.photoweatherapp.R
import com.andrew.photoweatherapp.domain.dataSources.apiClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val mainBottomNav by lazy { findViewById<BottomNavigationView>(R.id.main_bottomNavigationView) }
    private val mainNavController by lazy { findNavController(R.id.main_navigation_fragment) }
    private val hiddenBottomNav by lazy { listOf(R.id.splashFragment,R.id.cameraFragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainBottomNav.setupWithNavController(mainNavController)
        mainNavController.addOnDestinationChangedListener { _, destination, _ ->
            mainBottomNav.visibility =
                if (destination.id in hiddenBottomNav) View.GONE else View.VISIBLE
        }
        mainBottomNav.setOnNavigationItemReselectedListener {  }
    }
}