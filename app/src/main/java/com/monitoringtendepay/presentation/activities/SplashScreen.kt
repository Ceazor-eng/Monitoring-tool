package com.monitoringtendepay.presentation.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.monitoringtendepay.R
import com.monitoringtendepay.core.common.PreferenceManager

class SplashScreen : AppCompatActivity() {

    private lateinit var preferenceManager: PreferenceManager

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize PreferenceManager
        preferenceManager = PreferenceManager(this)

        // Show the splash screen
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Use a Handler to navigate after 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            // Check if it's the first launch
            val isFirstLaunch = preferenceManager.getBoolean(PreferenceManager.KEY_IS_FIRST_LAUNCH, true)

            if (isFirstLaunch) {
                // Update the flag in SharedPreferences
                preferenceManager.setBoolean(PreferenceManager.KEY_IS_FIRST_LAUNCH, false)
                // Navigate to Login activity
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            } else {
                // Check if user is logged in
                if (preferenceManager.isLoggedIn()) {
                    // Navigate to MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // Navigate to Login activity
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                }
            }
            finish()
        }, 3000)
    }
}