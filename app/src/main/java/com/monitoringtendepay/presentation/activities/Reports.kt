package com.monitoringtendepay.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
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
        val textGeneratePaymentsReport: TextView = view.findViewById(R.id.text_generate_payments_report)
        cardGeneratePaymentsReport.setOnClickListener {
            Log.d("ReportsFragment", "Payments card clicked")
            changeBackgroundAndTextColorTemporarily(cardGeneratePaymentsReport, textGeneratePaymentsReport, R.drawable.bg_white, R.drawable.bg1, R.color.purple_500, R.color.white)
            val intent = Intent(requireActivity(), FilterPayments::class.java)
            startActivity(intent)
        }

        val cardGenerateSessionsReport: RelativeLayout = view.findViewById(R.id.relative_sessions_report)
        val textGenerateSessionsReport: TextView = view.findViewById(R.id.text_generate_sessions_report)
        cardGenerateSessionsReport.setOnClickListener {
            Log.d("ReportsFragment", "Sessions card clicked")
            changeBackgroundAndTextColorTemporarily(cardGenerateSessionsReport, textGenerateSessionsReport, R.drawable.bg_white, R.drawable.bg1, R.color.purple_500, R.color.white)
            val intent = Intent(requireActivity(), FilterUssdSessions::class.java)
            startActivity(intent)
        }
    }

    private fun changeBackgroundAndTextColorTemporarily(view: View, textView: TextView, newBackground: Int, originalBackground: Int, newTextColor: Int, originalTextColor: Int) {
        view.setBackgroundResource(newBackground)
        textView.setTextColor(ContextCompat.getColor(requireContext(), newTextColor))
        Handler(Looper.getMainLooper()).postDelayed({
            view.setBackgroundResource(originalBackground)
            textView.setTextColor(ContextCompat.getColor(requireContext(), originalTextColor))
        }, 200) // Adjust the delay as needed
    }
}