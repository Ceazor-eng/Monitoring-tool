package com.monitoringtendepay.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.monitoringtendepay.R
import com.monitoringtendepay.core.common.PreferenceManager
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

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var scrollView: ScrollView
    private lateinit var recyclerView: RecyclerView
    private lateinit var paymentsAdapter: PaymentsAdapter
    private lateinit var completeMonthlyTransactions: TextView
    private lateinit var pendingMonthlyTransactions: TextView
    private lateinit var failedMonthlyTransactions: TextView
    private lateinit var missingPayments: TextView
    private lateinit var barChart: BarChart
    private lateinit var logOut: ImageView
    private lateinit var welcomeText: TextView
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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

        preferenceManager = PreferenceManager(requireContext())

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
        logOut()
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

    private fun logOut() {
        logOut.setOnClickListener {
            Log.d("DashboardFragment", "LogOut imageView clicked")

            // Clear preferences
            preferenceManager.clear()
            Log.d("DashboardFragment", "Preferences cleared")
            //

            // Navigate to Login activity and clear the back stack
            val intent = Intent(requireActivity(), Login::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun setUpViews(view: View) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        scrollView = view.findViewById(R.id.scroll_view)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        paymentsAdapter = PaymentsAdapter(emptyList())
        recyclerView.adapter = paymentsAdapter
        barChart = view.findViewById(R.id.barChart)
        welcomeText = view.findViewById(R.id.welcome_ian)
        logOut = view.findViewById(R.id.logout_image_view)

        completeMonthlyTransactions = view.findViewById(R.id.total_transactions_number_txt)
        pendingMonthlyTransactions = view.findViewById(R.id.pending_transactions_number_txt)
        failedMonthlyTransactions = view.findViewById(R.id.failed_Transactions_number_txt)
        missingPayments = view.findViewById(R.id.missing_transactions_number_txt)

        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        scrollView.setOnScrollChangeListener { v: View, scrollX: Int, scrollY: Int, _: Int, _: Int ->
            swipeRefreshLayout.isEnabled = scrollY == 0
        }
    }

    private fun refreshData() {
        fetchData()
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
        viewModel.fetchAllPayments("fetchAllPayments")

        lifecycleScope.launchWhenStarted {
            viewModel.paymentState.collect { state ->
                when (state.isLoading) {
                    true -> Log.d("Dashboard", "Loading...")
                    false -> {
                        state.payments?.let { payments ->
                            Log.d("Dashboard", "Payments response: $payments")
                            updateChartWithData(payments)
                        }
                        state.error?.let { errorMessage ->
                            Log.d("Dashboard", "Error: $errorMessage")
                        }
                    }
                }
            }
        }
    }

    private fun updateChartWithData(payments: List<AllPayments>?) {
        payments?.let { data ->
            data.forEach { payment ->
                Log.d("Dashboard", "Payment status: ${payment.paymentStatus}")
            }

            val totalSuccessfulPayments = data.count { it.paymentStatus == "1" }.toFloat()
            val totalFailedPayments = data.count { it.paymentStatus == "4" }.toFloat()
            val totalPendingPayments = data.count { it.paymentStatus == "2" }.toFloat()
            val totalMissingPayments = data.count { it.paymentStatus == "3" }.toFloat()

            Log.d("Dashboard", "Total Successful Payments: $totalSuccessfulPayments")
            Log.d("Dashboard", "Total Failed Payments: $totalFailedPayments")
            Log.d("Dashboard", "Total Pending Payments: $totalPendingPayments")
            Log.d("Dashboard", "Total Missing Payments: $totalMissingPayments")

            val entries = listOf(
                BarEntry(0f, totalSuccessfulPayments),
                BarEntry(1f, totalFailedPayments),
                BarEntry(2f, totalPendingPayments),
                BarEntry(3f, totalMissingPayments)
            )

            val dataSet = BarDataSet(entries, "Payment Status").apply {
                colors = listOf(
                    requireContext().getColor(R.color.green),
                    requireContext().getColor(R.color.red),
                    requireContext().getColor(R.color.orange),
                    requireContext().getColor(R.color.yellow)
                )
                valueTextColor = requireContext().getColor(R.color.black)
            }

            val barData = BarData(dataSet)
            barChart.data = barData

            barChart.xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(listOf("Success", "Failed", "Pending", "Missing"))
                granularity = 1f
                setDrawLabels(true)
            }

            barChart.invalidate()
        } ?: run {
            Log.d("Dashboard", "Payments data is null.")
        }
    }

    private fun observeMissingPayments() {
        viewModel.missingPaymentsState.onEach { state ->
            when {
                state.isLoading -> Log.d("Dashboard", "Loading...")
                state.error.isNotEmpty() -> {
                    Log.d("Dashboard", "Error: ${state.error}")
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }

                state.missingPayments != null -> {
                    Log.d("Dashboard", "Success: ${state.missingPayments.missingPayments}")
                    missingPayments.text = state.missingPayments.missingPayments
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeFailedTransactions() {
        viewModel.failedTransactionState.onEach { state ->
            when {
                state.isLoading -> Log.d("Dashboard", "Loading...")
                state.error.isNotEmpty() -> {
                    Log.d("Dashboard", "Error: ${state.error}")
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }

                state.failedTransactions != null -> {
                    Log.d("Dashboard", "Success: ${state.failedTransactions.failedPayments}")
                    failedMonthlyTransactions.text = state.failedTransactions.failedPayments
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observePendingTransactions() {
        viewModel.pendingTransactionsState.onEach { state ->
            when {
                state.isLoading -> Log.d("Dashboard", "Loading...")
                state.error.isNotEmpty() -> {
                    Log.d("Dashboard", "Error: ${state.error}")
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }

                state.pendingTransactions != null -> {
                    Log.d("Dashboard", "Success: ${state.pendingTransactions.pendingPayments}")
                    pendingMonthlyTransactions.text = state.pendingTransactions.pendingPayments
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observePayments() {
        viewModel.paymentState.onEach { state ->
            when {
                state.isLoading -> Log.d("Dashboard", "Loading...")
                state.error.isNotEmpty() -> {
                    Log.d("Dashboard", "Error: ${state.error}")
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }

                state.payments.isNotEmpty() -> {
                    Log.d("Dashboard", "Success: ${state.payments}")
                    paymentsAdapter.updateData(state.payments.sortedByDescending { it.transactionDate }.take(3))
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeCompleteTransactions() {
        viewModel.completeTransactionsState.onEach { state ->
            when {
                state.isLoading -> Log.d("Dashboard", "Loading...")
                state.error.isNotEmpty() -> {
                    Log.d("Dashboard", "Error: ${state.error}")
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }

                state.completeTransactions != null -> {
                    Log.d("Dashboard", "Success: ${state.completeTransactions.completePayments}")
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

        swipeRefreshLayout.isRefreshing = false
    }

    private fun enableEdgeToEdge() {
        activity?.window?.apply {
            decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
        }
    }
}