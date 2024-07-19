package com.monitoringtendepay.presentation.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class Dashboard : Fragment() {

    private val viewModel: AllPaymentsViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var paymentsAdapter: PaymentsAdapter
    private lateinit var completeMonthlyTransactions: TextView
    private lateinit var pendingMonthlyTransactions: TextView
    private lateinit var failedMonthlyTransactions: TextView
    private lateinit var missingPayments: TextView
    private lateinit var barChart: BarChart
    private lateinit var welcomeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews(view)
        updateGreetingMessage()

        observePayments()
        observeCompleteTransactions()
        observePendingTransactions()
        observeFailedTransactions()
        observeMissingPayments()
        fetchDataAndPopulateChart()
        setUpChart()
        fetchData()
    }

    private fun updateGreetingMessage() {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val greeting = when (hourOfDay) {
            in 0..11 -> "Good morning"
            in 12..17 -> "Good afternoon"
            else -> "Good evening"
        }
        welcomeText.text = "$greeting!"
    }

    private fun setUpViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        paymentsAdapter = PaymentsAdapter(emptyList())
        recyclerView.adapter = paymentsAdapter
        barChart = view.findViewById(R.id.barChart)
        welcomeText = view.findViewById(R.id.welcome_ian)

        completeMonthlyTransactions = view.findViewById(R.id.total_transactions_number_txt)
        pendingMonthlyTransactions = view.findViewById(R.id.pending_transactions_number_txt)
        failedMonthlyTransactions = view.findViewById(R.id.failed_Transactions_number_txt)
        missingPayments = view.findViewById(R.id.missing_transactions_number_txt)
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
                            // Log the entire response for debugging
                            Log.d("MainActivity", "Payments response: $payments")

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
            // Log each payment status for debugging
            data.forEach { payment ->
                Log.d("MainActivity", "Payment status: ${payment.paymentStatus}")
            }

            // Calculate the counts for each status
            val totalSuccessfulPayments = data.count { it.paymentStatus == "1" }.toFloat()
            val totalFailedPayments = data.count { it.paymentStatus == "4" }.toFloat()
            val totalPendingPayments = data.count { it.paymentStatus == "2" }.toFloat()
            val totalMissingPayments = data.count { it.paymentStatus == "3" }.toFloat()

            // Log the counts for debugging
            Log.d("MainActivity", "Total Successful Payments: $totalSuccessfulPayments")
            Log.d("MainActivity", "Total Failed Payments: $totalFailedPayments")
            Log.d("MainActivity", "Total Pending Payments: $totalPendingPayments")
            Log.d("MainActivity", "Total Missing Payments: $totalMissingPayments")

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
                requireContext().getColor(R.color.green),
                requireContext().getColor(R.color.red),
                requireContext().getColor(R.color.orange),
                requireContext().getColor(R.color.yellow)
            )
            dataSet.valueTextColor = requireContext().getColor(R.color.black)

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
            Log.d("DashboardFragment", "Payments data is null.")
        }
    }

    private fun observeMissingPayments() {
        viewModel.missingPaymentsState.onEach { state ->
            when {
                state.isLoading -> {
                    Log.d("DashboardFragment", "Loading...")
                }
                state.error.isNotEmpty() -> {
                    Log.d("DashboardFragment", "Error: ${state.error}")
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }
                state.missingPayments != null -> {
                    Log.d("DashboardFragment", "Success: ${state.missingPayments.missingPayments}")
                    missingPayments.text = state.missingPayments.missingPayments
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeFailedTransactions() {
        viewModel.failedTransactionState.onEach { state ->
            when {
                state.isLoading -> {
                    Log.d("DashboardFragment", "Loading...")
                }
                state.error.isNotEmpty() -> {
                    Log.d("DashboardFragment", "Error: ${state.error}")
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }
                state.failedTransactions != null -> {
                    Log.d("DashboardFragment", "Success: ${state.failedTransactions.failedPayments}")
                    failedMonthlyTransactions.text = state.failedTransactions.failedPayments
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observePendingTransactions() {
        viewModel.pendingTransactionsState.onEach { state ->
            when {
                state.isLoading -> {
                    Log.d("DashboardFragment", "Loading...")
                }
                state.error.isNotEmpty() -> {
                    Log.d("DashboardFragment", "Error: ${state.error}")
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }
                state.pendingTransactions != null -> {
                    Log.d("DashboardFragment", "Success: ${state.pendingTransactions.pendingPayments}")
                    pendingMonthlyTransactions.text = state.pendingTransactions.pendingPayments
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observePayments() {
        viewModel.paymentState.onEach { state ->
            when {
                state.isLoading -> {
                    Log.d("DashboardFragment", "Loading...")
                    // Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }
                state.error.isNotEmpty() -> {
                    Log.d("DashboardFragment", "Error: ${state.error}")
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }
                state.payments.isNotEmpty() -> {
                    Log.d("DashboardFragment", "Success: ${state.payments}")
                    paymentsAdapter.updateData(state.payments.sortedByDescending { it.transactionDate }.take(3))
                    // Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeCompleteTransactions() {
        viewModel.completeTransactionsState.onEach { state ->
            when {
                state.isLoading -> {
                    Log.d("DashboardFragment", "Loading...")
                }
                state.error.isNotEmpty() -> {
                    Log.d("DashboardFragment", "Error: ${state.error}")
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }
                state.completeTransactions != null -> {
                    Log.d("MainFragment", "Success: ${state.completeTransactions.completePayments}")
                    completeMonthlyTransactions.text = state.completeTransactions.completePayments
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun fetchData() {
        viewModel.fetchAllPayments("fetchAllPayments")
        viewModel.fetchCompleteMonthlyTransactions("fetchTotalCompletedPayments")
        viewModel.fetchPendingMonthlyTransactions("fetchTotalPendingPayments")
        viewModel.fetchFailedMonthlyTransactions("fetchTotalFailedPayments")
        viewModel.fetchMissingPayments("fetchTotalMissingPayments")
    }
}