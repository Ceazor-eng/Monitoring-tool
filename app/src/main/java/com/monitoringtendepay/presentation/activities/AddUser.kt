package com.monitoringtendepay.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.monitoringtendepay.R
import com.monitoringtendepay.presentation.states.RegisterState
import com.monitoringtendepay.presentation.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUser : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var etUsername: EditText
    private lateinit var fName: EditText
    private lateinit var lName: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var email: EditText
    private lateinit var registerBtn: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var roleSpinner: Spinner
    private lateinit var backButton: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fName = findViewById(R.id.fName)
        etUsername = findViewById(R.id.username)
        lName = findViewById(R.id.lName)
        phoneNumber = findViewById(R.id.phoneNumber)
        email = findViewById(R.id.email)
        registerBtn = findViewById(R.id.btnRegister)
        progressBar = findViewById(R.id.progressBar)
        backButton = findViewById(R.id.backButton)

        roleSpinner = findViewById(R.id.roleSpinner)
        val roles = arrayOf("Select Role", "Admin", "User")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinner.adapter = adapter

        backButton.setOnClickListener {
            finish()
        }

        registerBtn.setOnClickListener {
            val username = etUsername.text.toString()
            val fName = fName.text.toString()
            val lname = lName.text.toString()
            val phoneNumber = phoneNumber.text.toString()
            val email = email.text.toString()
            // Get selected role
            val selectedRole = roleSpinner.selectedItemPosition
            val roleId = when (selectedRole) {
                1 -> "1" // Admin
                2 -> "2" // User
                else -> ""
            }
            val action = "createUser"

            Log.d("RegisterUser", "Register button clicked with username: $username, fName: $fName, lname: $lname, phoneNumber: $phoneNumber, email: $email, roleId: $roleId")

            if (username.isNotEmpty() && fName.isNotEmpty() && lname.isNotEmpty() && phoneNumber.isNotEmpty() && email.isNotEmpty() && roleId.isNotEmpty()) {
                viewModel.registerUser(action, email, fName, lname, phoneNumber, roleId, username)
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.registerState.collect { authState ->
                handleAuthState(authState)
            }
        }
    }

    private fun handleAuthState(authState: RegisterState) {
        if (authState.isLoading) {
            progressBar.visibility = ProgressBar.VISIBLE
        } else {
            progressBar.visibility = ProgressBar.GONE
            authState.error?.let { error ->
                Log.d("RegisterUser", "AuthState data: $error")
                val intent = Intent(this, FailedActivity::class.java)
                intent.putExtra("responseMessage", error)
                startActivity(intent)
                finish()
            }
            authState.data?.let { data ->
                Log.d("RegisterUser", "AuthState data: $data")
                val intent = Intent(this, SuccessActivity::class.java)
                intent.putExtra("responseMessage", data.salutation)
                startActivity(intent)
                finish()
            }
        }
    }
}