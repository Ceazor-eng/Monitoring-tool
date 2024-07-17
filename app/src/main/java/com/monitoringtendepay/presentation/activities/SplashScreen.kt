package com.monitoringtendepay.presentation.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.monitoringtendepay.R

class SplashScreen : AppCompatActivity() {

    private lateinit var getStartedBtn: Button
    private lateinit var preferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        preferences = getSharedPreferences("com.monitoringtendepay", MODE_PRIVATE)
        val isFirstLaunch = preferences.getBoolean("isFirstLaunch", true)

        if (isFirstLaunch) {
            setContentView(R.layout.activity_splash_screen)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            getStartedBtn = findViewById(R.id.btnNext)
            getStartedBtn.setOnClickListener {
                // Update the flag in SharedPreferences
                preferences.edit().putBoolean("isFirstLaunch", false).apply()

                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            // Directly navigate to the Login activity
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun clearPreferences() {
        preferences.edit().clear().apply()
    }

}
