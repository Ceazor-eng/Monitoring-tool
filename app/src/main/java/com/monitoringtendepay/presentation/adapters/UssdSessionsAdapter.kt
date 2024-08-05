package com.monitoringtendepay.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.monitoringtendepay.R
import com.monitoringtendepay.domain.models.AllUssdSessions
import java.text.SimpleDateFormat
import java.util.Locale

class UssdSessionsAdapter(private var ussdSessions: List<AllUssdSessions>) : RecyclerView.Adapter<UssdSessionsAdapter.UssdSessionViewHolder>() {

    fun updateData(newUssdSessions: List<AllUssdSessions>) {
        ussdSessions = newUssdSessions
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UssdSessionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ussd, parent, false)
        return UssdSessionViewHolder(view)
    }

    override fun onBindViewHolder(holder: UssdSessionViewHolder, position: Int) {
        holder.bind(ussdSessions[position])
    }

    override fun getItemCount(): Int {
        return ussdSessions.size
    }

    class UssdSessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val initiatorPhoneTextView: TextView = itemView.findViewById(R.id.initiatorPhoneTextView)
        private val SessionIdTextView: TextView = itemView.findViewById(R.id.sessions_id_txt)
        private val dateTimeTextView: TextView = itemView.findViewById(R.id.dateTimeTextView)

        fun bind(ussdSession: AllUssdSessions) {
            initiatorPhoneTextView.text = ussdSession.msisdn
            SessionIdTextView.text = ussdSession.sessionId
            dateTimeTextView.text = formatDate(ussdSession.sessionsDate)
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