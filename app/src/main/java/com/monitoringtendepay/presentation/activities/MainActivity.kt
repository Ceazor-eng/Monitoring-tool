package com.monitoringtendepay.presentation.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monitoringtendepay.R
import com.monitoringtendepay.presentation.adapters.UssdSessionsAdapter
import com.monitoringtendepay.presentation.viewmodels.AllUssdSessionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: AllUssdSessionsViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var ussdSessionAdapter: UssdSessionsAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        ussdSessionAdapter = UssdSessionsAdapter(emptyList())
        recyclerView.adapter = ussdSessionAdapter

        observePayments()
        viewModel.fetchAllUssdSessions("fetchSessionMonitoring")
    }

    private fun observePayments() {
        viewModel.ussdSessionsState.onEach { state ->
            when {
                state.isLoading -> {
                    Log.d("MainActivity", "Loading...")
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                }
                state.error.isNotEmpty() -> {
                    Log.d("MainActivity", "Error: ${state.error}")
                    Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
                }
                state.ussdSessions.isNotEmpty() -> {
                    Log.d("MainActivity", "Success: ${state.ussdSessions}")
                    ussdSessionAdapter.updateData(state.ussdSessions)
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                }
            }
        }.launchIn(lifecycleScope)
    }
}