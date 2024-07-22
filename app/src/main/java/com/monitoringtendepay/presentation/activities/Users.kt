package com.monitoringtendepay.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monitoringtendepay.R
import com.monitoringtendepay.presentation.adapters.AllUsersAdapter
import com.monitoringtendepay.presentation.viewmodels.AllUsersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class Users : Fragment() {

    private val viewModel: AllUsersViewModel by viewModels()
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

        val addUserBtn = view.findViewById<Button>(R.id.AddUserBtn)
        val recyclerView = view.findViewById<RecyclerView>(R.id.usersRecyclerView)

        adapter = AllUsersAdapter(emptyList()) // Initialize the adapter

        addUserBtn.setOnClickListener {
            val intent = Intent(requireContext(), AddUser::class.java)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        observeUsers()
        fetchData()
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
    }
}