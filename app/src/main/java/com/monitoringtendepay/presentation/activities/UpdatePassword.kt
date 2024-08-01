
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
import com.monitoringtendepay.core.common.hashPassword
import com.monitoringtendepay.presentation.states.AuthState
import com.monitoringtendepay.presentation.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdatePassword : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var username: String
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var btnUpdatePassword: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // username = findViewById(R.id.username_update_password)
        password = findViewById(R.id.update_password)
        confirmPassword = findViewById(R.id.confirm_Password)
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword)
        progressBar = findViewById(R.id.progressBar)

        // Get the username from the intent extras
        val intentUsername = intent.getStringExtra("username")
        if (intentUsername != null) {
            username = intentUsername
        } else {
            Toast.makeText(this, "Error: Username not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        lifecycleScope.launchWhenStarted {
            authViewModel.updatePasswordState.collect { authState ->
                handleAuthState(authState)
            }
        }

        btnUpdatePassword.setOnClickListener {
            val newPassword = password.text.toString()
            val confirmPasswordText = confirmPassword.text.toString()

            if (newPassword.isNotEmpty() && newPassword == confirmPasswordText) {
                val hashedPassword = hashPassword(newPassword)
                val action = "updatePassword"
                Log.d("UpdatePasswordActivity", "Update password button clicked with newPassword: $hashedPassword")
                authViewModel.updatePassword(action, username, hashedPassword)
            } else {
                if (newPassword.isEmpty()) {
                    Toast.makeText(this, "Please enter a new password", Toast.LENGTH_SHORT).show()
                } else if (newPassword != confirmPasswordText) {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleAuthState(authState: AuthState) {
        progressBar.visibility = if (authState.isLoading) View.VISIBLE else View.GONE
        authState.data?.let { data ->
            Log.d("UpdatePasswordActivity", "AuthState data: $data")
            if (isUpdatePasswordSuccessful(data)) {
                Log.d("UpdatePasswordActivity", "Update password successful")
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                navigateToHomeScreen() // Navigate to Home screen on success
            } else {
                Log.d("UpdatePasswordActivity", "Update password failed")
                Toast.makeText(this, "Update password failed", Toast.LENGTH_SHORT).show()
            }
        }
        authState.error?.let { error ->
            Log.d("UpdatePasswordActivity", "Error: $error")
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isUpdatePasswordSuccessful(data: String): Boolean {
        return data.contains("status=success")
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}