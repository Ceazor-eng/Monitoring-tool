package com.monitoringtendepay.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.monitoringtendepay.R
import com.monitoringtendepay.presentation.states.AuthState
import com.monitoringtendepay.presentation.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Login : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)

        lifecycleScope.launchWhenStarted {
            authViewModel.loginState.collect { authState ->
                handleAuthState(authState)
            }
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
//            val hashedPassword = hashPassword(password)
            // val action = "userLogin"
            Log.d("LoginActivity", "Login button clicked with username: $username,hashed password: $password")
            if (username.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.login(username, password)
            } else {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleAuthState(authState: AuthState) {
        if (authState.isLoading) {
            progressBar.visibility = ProgressBar.VISIBLE
        } else {
            progressBar.visibility = ProgressBar.GONE
            authState.error?.let { error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
            authState.data?.let { data ->
                Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
                if (data == "LogIn Successfully") {
                    navigateToHomeScreen()
                }
            }
        }
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}