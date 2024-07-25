package com.monitoringtendepay.presentation.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.monitoringtendepay.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_nav_view)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_dashboard -> {
                    loadFragment(Dashboard())
                    true
                }
                R.id.menu_reports -> {
                    loadFragment(Reports())
                    true
                }
                R.id.menu_users -> {
                    loadFragment(Users())
                    true
                }
                R.id.menu_profile -> {
                    loadFragment(History())
                    true
                }
                else -> false
            }
        }

        // Load the initial fragment
        if (savedInstanceState == null) {
            loadFragment(Dashboard())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}