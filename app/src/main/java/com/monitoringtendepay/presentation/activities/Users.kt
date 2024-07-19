package com.monitoringtendepay.presentation.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.monitoringtendepay.R
import com.monitoringtendepay.presentation.states.AuthState
import com.monitoringtendepay.presentation.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Users : Fragment() {

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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progressBar)

        setUpViews(view)

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
                Toast.makeText(requireContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.registerState.collect { authState ->
                handleAuthState(authState)
            }
        }
    }

    private fun setUpViews(view: View) {
        fName = view.findViewById(R.id.fName)
        etUsername = view.findViewById(R.id.username)
        lName = view.findViewById(R.id.lName)
        phoneNumber = view.findViewById(R.id.phoneNumber)
        email = view.findViewById(R.id.email)
        roleId = view.findViewById(R.id.roleID)
        registerBtn = view.findViewById(R.id.btnRegister)
    }

    private fun handleAuthState(authState: AuthState) {
        progressBar.visibility = if (authState.isLoading) View.VISIBLE else View.GONE
        authState.data?.let { data ->
            if (isRegisterSuccessful(data)) {
                Log.d("RegisterUser", "Register successful")
                Toast.makeText(requireContext(), "Register successful", Toast.LENGTH_SHORT).show()
                // navigateToHomeScreen()
            } else {
                Log.d("RegisterUser", "Register failed")
                // Toast.makeText(requireContext(), "Register failed", Toast.LENGTH_SHORT).show()
            }
        }
        authState.error?.let { error ->
            Log.d("RegisterUser", "Register error: $error")
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isRegisterSuccessful(data: String): Boolean {
        return data.contains("success")
    }
}