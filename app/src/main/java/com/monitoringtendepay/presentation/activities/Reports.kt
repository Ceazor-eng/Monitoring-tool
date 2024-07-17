package com.monitoringtendepay.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.monitoringtendepay.R

class Reports : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reports, container, false)
        Log.d("ReportsFragment", "Fragment view created")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ReportsFragment", "onViewCreated called")

        val cardGeneratePaymentsReport: RelativeLayout = view.findViewById(R.id.relative_payments_report)
        cardGeneratePaymentsReport.setOnClickListener {
            Log.d("ReportsFragment", "Payments card clicked")
            val intent = Intent(requireActivity(), FilterPayments::class.java)
            startActivity(intent)
        }

        val cardGenerateSessionsReport: RelativeLayout = view.findViewById(R.id.relative_sessions_report)
        cardGenerateSessionsReport.setOnClickListener {
            Log.d("ReportsFragment", "Sessions card clicked")
            val intent = Intent(requireActivity(), FilterUssdSessions::class.java)
            startActivity(intent)
        }
    }
}