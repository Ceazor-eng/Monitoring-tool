package com.monitoringtendepay.presentation.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
class SessionsFragment : Fragment() {

    private val sessionsViewModel: AllUssdSessionsViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var sessionsAdapter: UssdSessionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sessions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews(view)
        observeSessions()
        fetchData()

        val cardPayments: RelativeLayout = view.findViewById(R.id.payments_card)
        cardPayments.setOnClickListener {
            Log.d("SessionsFragment", "Payments card clicked")
            // Replace the fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, History())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setUpViews(view: View) {
        recyclerView = view.findViewById(R.id.ussd_rec_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        sessionsAdapter = UssdSessionsAdapter(emptyList())
        recyclerView.adapter = sessionsAdapter
    }

    private fun observeSessions() {
        sessionsViewModel.ussdSessionsState.onEach { state ->
            when {
                state.isLoading -> {
                    Log.d("HistoryFragment", "Loading sessions...")
                }

                state.error.isNotEmpty() -> {
                    Log.d("HistoryFragment", "Error: ${state.error}")
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }

                state.ussdSessions.isNotEmpty() -> {
                    Log.d("HistoryFragment", "Success: ${state.ussdSessions}")
                    sessionsAdapter.updateData(state.ussdSessions.sortedByDescending { it.sessionsDate })
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun fetchData() {
        sessionsViewModel.fetchAllUssdSessions("fetchSessionMonitoring")
    }
}