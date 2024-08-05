package com.monitoringtendepay.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.monitoringtendepay.R
import com.monitoringtendepay.domain.models.AllPayments
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryPaymentsAdapter(private var payments: List<AllPayments>) : RecyclerView.Adapter<HistoryPaymentsAdapter.HistoryPaymentViewHolder>() {

    fun updatePayments(newPayments: List<AllPayments>) {
        payments = newPayments.sortedByDescending { it.transactionDate }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryPaymentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_payment, parent, false)
        return HistoryPaymentViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryPaymentViewHolder, position: Int) {
        holder.bind(payments[position])
    }

    override fun getItemCount(): Int {
        return payments.size
    }

    class HistoryPaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val initiatorPhoneTextView: TextView = itemView.findViewById(R.id.initiatorPhoneTextView)
        private val mpesaRefSessionIdTextView: TextView = itemView.findViewById(R.id.mpesaRefSessionIdTextView)
        private val paymentStatusTextView: TextView = itemView.findViewById(R.id.paymentStatusTextView)
        private val dateTimeTextView: TextView = itemView.findViewById(R.id.dateTimeTextView)

        fun bind(payment: AllPayments) {
            initiatorPhoneTextView.text = payment.initiatorPhone
            mpesaRefSessionIdTextView.text = payment.mpesaRef
            paymentStatusTextView.text = getStatusText(payment.paymentStatus)
            paymentStatusTextView.setTextColor(getStatusColor(payment.paymentStatus))
            dateTimeTextView.text = formatDate(payment.transactionDate)
        }

        private fun getStatusText(status: String): String {
            return when (status.toIntOrNull()) {
                1 -> "Success"
                2 -> "Pending"
                3 -> "Missing"
                4 -> "Failed"
                else -> "Unknown"
            }
        }

        private fun getStatusColor(status: String): Int {
            return when (status.toIntOrNull()) {
                1 -> itemView.context.getColor(R.color.green)
                2 -> itemView.context.getColor(R.color.orange)
                3 -> itemView.context.getColor(R.color.yellow)
                4 -> itemView.context.getColor(R.color.red)
                else -> itemView.context.getColor(R.color.black)
            }
        }

        // Utility function to format the date
        private fun formatDate(dateString: String): String {
            return try {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = sdf.parse(dateString)
                val formattedSdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                formattedSdf.format(date!!)
            } catch (e: Exception) {
                dateString
            }
        }
    }
}