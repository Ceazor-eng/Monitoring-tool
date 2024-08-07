package com.monitoringtendepay.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.monitoringtendepay.R
import com.monitoringtendepay.core.common.PreferenceManager
import com.monitoringtendepay.core.common.hashPassword
import com.monitoringtendepay.presentation.states.LoginState
import com.monitoringtendepay.presentation.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Login : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: LinearLayout
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

        preferenceManager = PreferenceManager(this)

        // Check if user is already logged in
        if (preferenceManager.isLoggedIn()) {
            navigateToHomeScreen()
            return
        }

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.password)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)

        lifecycleScope.launch {
            authViewModel.loginState.collect { authState ->
                handleLoginState(authState)
            }
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            val hashedPassword = hashPassword(password)
            val action = "userLogin"
            Log.d("LoginActivity", "Login button clicked with username: $username, hashedPassword: $hashedPassword")
            if (username.isNotEmpty() && hashedPassword.isNotEmpty()) {
                authViewModel.login(action, username, hashedPassword)
            } else {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleLoginState(loginState: LoginState) {
        progressBar.visibility = if (loginState.isLoading) View.VISIBLE else View.GONE
        loginState.data?.let { data ->
            Log.d("LoginActivity", "LoginState data: $data")
            if (loginState.changePasswordRequired) {
                Log.d("LoginActivity", "Redirecting to change password screen")
                navigateToChangePasswordScreen(data.username)
            } else if (isLoginSuccessful(data.status)) {
                // Save login state and session token to SharedPreferences
                preferenceManager.setLoggedIn(true)
                preferenceManager.setSessionToken(data.sessionToken)
                preferenceManager.setUsername(data.username)
                preferenceManager.setRole(data.role)
                Log.d("LoginActivity", "Session token saved: ${preferenceManager.getSessionToken()}")

                Log.d("LoginActivity", "Login successful")
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("role", data.role)
                }
                startActivity(intent)
                finish()
            } else {
                Log.d("LoginActivity", "Login failed")
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
        loginState.error?.let { error ->
            Log.d("LoginActivity", "Error: $error")
            val intent = Intent(this, FailedActivity::class.java).apply {
                putExtra("responseMessage", error)
            }
            startActivity(intent)
            finish()
            // Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isLoginSuccessful(status: String): Boolean {
        return status == "success"
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToChangePasswordScreen(username: String?) {
        Log.d("LoginActivity", "Navigating to update password screen with username: $username")
        val intent = Intent(this, UpdatePassword::class.java)
        startActivity(intent)
        finish()
    }
}