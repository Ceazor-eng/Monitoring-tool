package com.monitoringtendepay.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
        etPassword = findViewById(R.id.password)
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
            val action = "userLogin"
            Log.d("LoginActivity", "Login button clicked with username: $username, password: $password")
            if (username.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.login(action, username, password)
            } else {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleAuthState(authState: AuthState) {
        progressBar.visibility = if (authState.isLoading) View.VISIBLE else View.GONE
        authState.data?.let { data ->
            Log.d("LoginActivity", "AuthState data: $data")
            if (isLoginSuccessful(data)) {
                Log.d("LoginActivity", "Login successful")
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                navigateToHomeScreen()
            } else {
                Log.d("LoginActivity", "Login failed")
                //  Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
        authState.error?.let { error ->
            Log.d("LoginActivity", "Error: $error")
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isLoginSuccessful(data: String): Boolean {
        return data.contains("status=success")
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}