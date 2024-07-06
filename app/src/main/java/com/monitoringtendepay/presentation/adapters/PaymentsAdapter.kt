package com.monitoringtendepay.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.monitoringtendepay.R
import com.monitoringtendepay.domain.models.AllPayments

class PaymentsAdapter(private var payments: List<AllPayments>) : RecyclerView.Adapter<PaymentsAdapter.PaymentViewHolder>() {

    fun updateData(newPayments: List<AllPayments>) {
        payments = newPayments
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_payment, parent, false)
        return PaymentViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.bind(payments[position])
    }

    override fun getItemCount(): Int {
        return payments.size
    }

    class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val serviceCodeTextView: TextView = itemView.findViewById(R.id.serviceCodeTextView)
        private val initiatorPhoneTextView: TextView = itemView.findViewById(R.id.initiatorPhoneTextView)
        private val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
        private val mpesaRefSessionIdTextView: TextView = itemView.findViewById(R.id.mpesaRefSessionIdTextView)
        private val paymentStatusTextView: TextView = itemView.findViewById(R.id.paymentStatusTextView)
        private val dateTimeTextView: TextView = itemView.findViewById(R.id.dateTimeTextView)

        fun bind(payment: AllPayments) {
            serviceCodeTextView.text = "Service Code: ${payment.serviceCode}"
            initiatorPhoneTextView.text = "Initiator Phone: ${payment.initiatorPhone}"
            amountTextView.text = "Amount: ${payment.amount}"
            mpesaRefSessionIdTextView.text = "Mpesa Ref Session Id: ${payment.mpesaRef}"
            paymentStatusTextView.text = "Payment Status: ${payment.paymentStatus}"
            dateTimeTextView.text = "Date and Time: ${payment.transactionDate}"
        }
    }
}