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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.monitoringtendepay.R
import com.monitoringtendepay.presentation.states.AuthState
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
    private lateinit var roleId: EditText
    private lateinit var registerBtn: Button
    private lateinit var progressBar: ProgressBar

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
        roleId = findViewById(R.id.roleID)
        registerBtn = findViewById(R.id.btnRegister)
        progressBar = findViewById(R.id.progressBar)

        registerBtn.setOnClickListener {
            val username = etUsername.text.toString()
            val fName = fName.text.toString()
            val lname = lName.text.toString()
            val phoneNumber = phoneNumber.text.toString()
            val email = email.text.toString()
            val roleId = roleId.text.toString()
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
            }
        }
    }
}