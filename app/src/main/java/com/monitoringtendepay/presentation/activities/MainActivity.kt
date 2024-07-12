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
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.monitoringtendepay.R
import com.monitoringtendepay.domain.models.AllPayments
import com.monitoringtendepay.presentation.adapters.PaymentsAdapter
import com.monitoringtendepay.presentation.viewmodels.AllPaymentsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: AllPaymentsViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var paymentsAdapter: PaymentsAdapter
    private lateinit var completeMonthlyTransactions: TextView
    private lateinit var pendingMonthlyTransactions: TextView
    private lateinit var failedMonthlyTransactions: TextView
    private lateinit var missingPayments: TextView
    private lateinit var barChart: BarChart

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
        barChart = findViewById(R.id.barChart)

        completeMonthlyTransactions = findViewById(R.id.total_transactions_number_txt)
        pendingMonthlyTransactions = findViewById(R.id.pending_transactions_number_txt)
        failedMonthlyTransactions = findViewById(R.id.failed_Transactions_number_txt)
        missingPayments = findViewById(R.id.missing_transactions_number_txt)

        observePayments()
        observeCompleteTransactions()
        observePendingTransactions()
        observeFailedTransactions()
        observeMissingPayments()
        fetchDataAndPopulateChart()
        setUpChart()
        viewModel.fetchAllPayments("fetchAllPayments")
        viewModel.fetchCompleteMonthlyTransactions("fetchTotalCompletedPayments")
        viewModel.fetchPendingMonthlyTransactions("fetchTotalPendingPayments")
        viewModel.fetchFailedMonthlyTransactions("fetchTotalFailedPayments")
        viewModel.fetchMissingPayments("fetchTotalMissingPayments")
    }

    private fun setUpChart() {
        // Configure chart settings
        barChart.apply {
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = object : IAxisValueFormatter {
                private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                override fun getFormattedValue(value: Float, axis: AxisBase): String {
                    val millis = value.toLong()
                    return dateFormat.format(Date(millis))
                }
            }
        }
    }

    private fun fetchDataAndPopulateChart() {
        val action = "fetchAllPayments"

        viewModel.fetchAllPayments(action)

        lifecycleScope.launchWhenStarted {
            viewModel.paymentState.collect { state ->
                when (state.isLoading) {
                    true -> {
                        Log.d("MainActivity", "Loading...")
                    }
                    false -> {
                        state.payments?.let { payments ->
                            // Update chart with fetched data
                            updateChartWithData(payments)
                        }
                        state.error?.let { errorMessage ->
                            Log.d("MainActivity", "Error: ${state.error}")
                        }
                    }
                }
            }
        }
    }

    private fun updateChartWithData(payments: List<AllPayments>?) {
        payments?.let { data ->
            // Calculate the counts for each status
            val totalSuccessfulPayments = data.count { it.paymentStatus.toIntOrNull() == 1 }.toFloat()
            val totalFailedPayments = data.count { it.paymentStatus.toIntOrNull() == 4 }.toFloat()
            val totalPendingPayments = data.count { it.paymentStatus.toIntOrNull() == 2 }.toFloat()
            val totalMissingPayments = data.count { it.paymentStatus.toIntOrNull() == 3 }.toFloat()

            // Create entries for the bar chart
            val entries = listOf(
                BarEntry(0f, totalSuccessfulPayments),
                BarEntry(1f, totalFailedPayments),
                BarEntry(2f, totalPendingPayments),
                BarEntry(3f, totalMissingPayments)
            )

            // Create a BarDataSet with the entries
            val dataSet = BarDataSet(entries, "Payment Status")
            dataSet.colors = listOf(
                getColor(R.color.green), // Successful Payments
                getColor(R.color.red), // Failed Payments
                getColor(R.color.orange), // Pending Payments
                getColor(R.color.yellow) // Missing Payments
            )
            dataSet.valueTextColor = getColor(R.color.black)

            // Create BarData object with the dataset
            val barData = BarData(dataSet)

            // Set the data to your BarChart
            barChart.data = barData

            // Customize the X-axis to display labels
            val xAxis = barChart.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(listOf("Success", "Failed", "Pending", "Missing"))
            xAxis.granularity = 1f
            xAxis.setDrawLabels(true)

            // Refresh the chart
            barChart.invalidate()
        } ?: run {
            Log.d("MainActivity", "Payments data is null.")
        }
    }

    private fun observeMissingPayments() {
        viewModel.missingPaymentsState.onEach { state ->
            when {
                state.isLoading -> {
                    Log.d("MainActivity", "Loading...")
                    // Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                }
                state.error.isNotEmpty() -> {
                    Log.d("MainActivity", "Error: ${state.error}")
                    Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
                }
                state.missingPayments != null -> {
                    Log.d("MainActivity", "Success: ${state.missingPayments.missingPayments}")
                    missingPayments.text = state.missingPayments.missingPayments
                    // Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeFailedTransactions() {
        viewModel.failedTransactionState.onEach { state ->
            when {
                state.isLoading -> {
                    Log.d("MainActivity", "Loading...")
                    // Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                }
                state.error.isNotEmpty() -> {
                    Log.d("MainActivity", "Error: ${state.error}")
                    Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
                }
                state.failedTransactions != null -> {
                    Log.d("MainActivity", "Success: ${state.failedTransactions.failedPayments}")
                    failedMonthlyTransactions.text = state.failedTransactions.failedPayments
                    // Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                }
            }
        }.launchIn(lifecycleScope)
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
                    paymentsAdapter.updateData(state.payments.sortedByDescending { it.transactionDate }.take(3))
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