package com.monitoringtendepay.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.monitoringtendepay.R
import com.monitoringtendepay.presentation.adapters.AllUsersAdapter
import com.monitoringtendepay.presentation.states.AuthState
import com.monitoringtendepay.presentation.viewmodels.AllUsersViewModel
import com.monitoringtendepay.presentation.viewmodels.UserActionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class Users : Fragment() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var scrollView: ScrollView
    private lateinit var searchInput: EditText
    private val viewModel: AllUsersViewModel by viewModels()
    private val userActionsViewModel: UserActionsViewModel by viewModels()
    private lateinit var adapter: AllUsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addUserBtn = view.findViewById<LinearLayout>(R.id.AddUserBtn)
        val recyclerView = view.findViewById<RecyclerView>(R.id.usersRecyclerView)
        searchInput = view.findViewById(R.id.searchUser)

        adapter = AllUsersAdapter(emptyList(), userActionsViewModel) // Initialize the adapter

        addUserBtn.setOnClickListener {
            val intent = Intent(requireContext(), AddUser::class.java)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        observeUsers()
        lifecycleScope.launchWhenStarted {
            userActionsViewModel.makeAdminState.collect { authState ->
                observeMakeUserAdminAction(authState)
            }
        }
        lifecycleScope.launchWhenStarted {
            userActionsViewModel.resendOtpState.collect { authState ->
                observeResendOtpAction(authState)
            }
        }
        lifecycleScope.launchWhenStarted {
            userActionsViewModel.activateUserState.collect { authState ->
                observeActivateUserAction(authState)
            }
        }
        lifecycleScope.launchWhenStarted {
            userActionsViewModel.deactivateUserState.collect { authState ->
                observeDeactivateUserAction(authState)
            }
        }

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        scrollView = view.findViewById(R.id.Scroll)

        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        scrollView.setOnScrollChangeListener { v: View, scrollX: Int, scrollY: Int, _: Int, _: Int ->
            swipeRefreshLayout.isEnabled = scrollY == 0
        }

        searchInput.addTextChangedListener { text ->
            filterUsers(text.toString())
        }

        fetchData()
    }

    private fun filterUsers(query: String) {
        val filteredList = viewModel.usersState.value.users.filter { user ->
            user.username.contains(query, ignoreCase = true) || user.status.contains(query, ignoreCase = true) || user.role.contains(query, ignoreCase = true)
        }
        adapter.updateData(filteredList)
        if (filteredList.isEmpty()) {
            Toast.makeText(requireContext(), "No users found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun refreshData() {
        fetchData()
    }

    private fun observeDeactivateUserAction(authState: AuthState) {
        //  progressBar.visibility = if (authState.isLoading) View.VISIBLE else View.GONE
        authState.data?.let { data ->
            Log.d("UserActionsFragment", "AuthState data: $data")
            if (isActionSuccessfully(data)) {
                Log.d("UserActionsFragment", "Deactivate User successful")
                Toast.makeText(requireContext(), "Deactivate User  successfully", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("UserActionsFragment", "Deactivate User  failed")
                Toast.makeText(requireContext(), "Deactivate User failed", Toast.LENGTH_SHORT).show()
            }
        }
        authState.error?.let { error ->
            Log.d("UsersFragment", "Error: $error")
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeActivateUserAction(authState: AuthState) {
        //  progressBar.visibility = if (authState.isLoading) View.VISIBLE else View.GONE
        authState.data?.let { data ->
            Log.d("UserActionsFragment", "AuthState data: $data")
            if (isActionSuccessfully(data)) {
                Log.d("UserActionsFragment", "Activate User successful")
                Toast.makeText(requireContext(), "Activate User  successfully", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("UserActionsFragment", "Activate User  failed")
                Toast.makeText(requireContext(), "Activate User failed", Toast.LENGTH_SHORT).show()
            }
        }
        authState.error?.let { error ->
            Log.d("UsersFragment", "Error: $error")
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeResendOtpAction(authState: AuthState) {
        //  progressBar.visibility = if (authState.isLoading) View.VISIBLE else View.GONE
        authState.data?.let { data ->
            Log.d("UserActionsFragment", "AuthState data: $data")
            if (isActionSuccessfully(data)) {
                Log.d("UserActionsFragment", "Resend otp successful")
                Toast.makeText(requireContext(), "Resend otp  successfully", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("UserActionsFragment", "Resend otp  failed")
                Toast.makeText(requireContext(), "Resend otp  failed", Toast.LENGTH_SHORT).show()
            }
        }
        authState.error?.let { error ->
            Log.d("UsersFragment", "Error: $error")
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeMakeUserAdminAction(authState: AuthState) {
        //  progressBar.visibility = if (authState.isLoading) View.VISIBLE else View.GONE
        authState.data?.let { data ->
            Log.d("UserActionsFragment", "AuthState data: $data")
            if (isActionSuccessfully(data)) {
                Log.d("UserActionsFragment", "Make user admin successful")
                Toast.makeText(requireContext(), "Make user admin  successfully", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("UserActionsFragment", "Make user admin  failed")
                Toast.makeText(requireContext(), "Make user admin  failed", Toast.LENGTH_SHORT).show()
            }
        }
        authState.error?.let { error ->
            Log.d("UserActionsFragment", "Error: $error")
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isActionSuccessfully(data: String): Boolean {
        return data.contains("status=success")
    }

    private fun observeUsers() {
        viewModel.usersState.onEach { state ->
            when {
                state.isLoading -> {
                    Log.d("UsersFragment", "Loading...")
                    // Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }

                state.error.isNotEmpty() -> {
                    Log.d("UsersFragment", "Error: ${state.error}")
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }

                state.users.isNotEmpty() -> {
                    Log.d("UsersFragment", "Success: ${state.users}")
                    adapter.updateData(state.users)
                    // Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun fetchData() {
        viewModel.fetchAllUsers("fetchAllUsers")
        swipeRefreshLayout.isRefreshing = false
    }
}