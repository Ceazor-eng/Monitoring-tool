package com.monitoringtendepay.presentation.activities

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
import com.monitoringtendepay.presentation.states.LoginState
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

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            val action = "userLogin"
            Log.d("LoginActivity", "Login button clicked with username: $username, password: $password, action: $action")
            if (username.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.loginUser(username, password, action)
            } else {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            authViewModel.loginState.collect { state ->
                handleLoginState(state)
            }
        }
    }

    private fun handleLoginState(state: LoginState) {
        when {
            state.isLoading -> {
                Log.d("LoginActivity", "Loading...")
                progressBar.visibility = ProgressBar.VISIBLE
                btnLogin.isEnabled = false
            }
            state.success != null -> {
                Log.d("LoginActivity", "Login successful: ${state.success}")
                progressBar.visibility = ProgressBar.GONE
                btnLogin.isEnabled = true
                Toast.makeText(this, "Login successful: ${state.success}", Toast.LENGTH_LONG).show()
                // Navigate to next screen
            }
            state.error.isNotEmpty() -> {
                Log.e("LoginActivity", "Error: ${state.error}")
                progressBar.visibility = ProgressBar.GONE
                btnLogin.isEnabled = true
                Toast.makeText(this, "Error: ${state.error}", Toast.LENGTH_LONG).show()
            }
        }
    }
}