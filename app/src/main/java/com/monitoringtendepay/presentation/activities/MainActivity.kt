package com.monitoringtendepay.presentation.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
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
import com.monitoringtendepay.presentation.adapters.PaymentsAdapter
import com.monitoringtendepay.presentation.viewmodels.AllPaymentsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: AllPaymentsViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var paymentsAdapter: PaymentsAdapter
    private lateinit var completeMonthlyTransactions: TextView
    private lateinit var pendingMonthlyTransactions: TextView

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
        paymentsAdapter = PaymentsAdapter(emptyList())
        recyclerView.adapter = paymentsAdapter

        completeMonthlyTransactions = findViewById(R.id.total_transactions_number_txt)
        pendingMonthlyTransactions = findViewById(R.id.pending_transactions_number_txt)

        observePayments()
        observeCompleteTransactions()
        observePendingTransactions()
        viewModel.fetchAllPayments("fetchAllPayments")
        viewModel.fetchCompleteMonthlyTransactions("fetchTotalCompletedPayments")
        viewModel.fetchPendingMonthlyTransactions("fetchTotalPendingPayments")
    }

    private fun observePendingTransactions() {
        viewModel.pendingTransactionsState.onEach { state ->
            when {
                state.isLoading -> {
                    Log.d("MainActivity", "Loading...")
                    // Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                }
                state.error.isNotEmpty() -> {
                    Log.d("MainActivity", "Error: ${state.error}")
                    Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
                }
                state.pendingTransactions != null -> {
                    Log.d("MainActivity", "Success: ${state.pendingTransactions.pendingPayments}")
                    pendingMonthlyTransactions.text = state.pendingTransactions.pendingPayments
                    // Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun observePayments() {
        viewModel.paymentState.onEach { state ->
            when {
                state.isLoading -> {
                    Log.d("MainActivity", "Loading...")
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                }
                state.error.isNotEmpty() -> {
                    Log.d("MainActivity", "Error: ${state.error}")
                    Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
                }
                state.payments.isNotEmpty() -> {
                    Log.d("MainActivity", "Success: ${state.payments}")
                    paymentsAdapter.updateData(state.payments)
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeCompleteTransactions() {
        viewModel.completeTransactionsState.onEach { state ->
            when {
                state.isLoading -> {
                    Log.d("MainActivity", "Loading...")
                    //   Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                }
                state.error.isNotEmpty() -> {
                    Log.d("MainActivity", "Error: ${state.error}")
                    Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
                }
                state.completeTransactions != null -> {
                    Log.d("MainActivity", "Success: ${state.completeTransactions.completePayments}")
                    completeMonthlyTransactions.text = state.completeTransactions.completePayments
                    // Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                }
            }
        }.launchIn(lifecycleScope)
    }
}