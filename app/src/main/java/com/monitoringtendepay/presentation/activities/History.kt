package com.monitoringtendepay.presentation.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
class History : Fragment() {

    private val viewModel: AllPaymentsViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var paymentsAdapter: PaymentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews(view)
        observePayments()
        fetchData()

        val cardSessions: RelativeLayout = view.findViewById(R.id.sessions_card)
        cardSessions.setOnClickListener {
            Log.d("HistoryFragment", "Sessions card clicked")
            // Replace the fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SessionsFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setUpViews(view: View) {
        recyclerView = view.findViewById(R.id.payments_rec_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        paymentsAdapter = PaymentsAdapter(emptyList())
        recyclerView.adapter = paymentsAdapter
    }

    private fun observePayments() {
        viewModel.paymentState.onEach { state ->
            when {
                state.isLoading -> {
                    Log.d("HistoryFragment", "Loading...")
                    // Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }

                state.error.isNotEmpty() -> {
                    Log.d("HistoryFragment", "Error: ${state.error}")
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }

                state.payments.isNotEmpty() -> {
                    Log.d("HistoryFragment", "Success: ${state.payments}")
                    paymentsAdapter.updatePayments(state.payments.sortedByDescending { it.transactionDate })
                    // Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun fetchData() {
        viewModel.fetchAllPayments("fetchAllPayments")
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