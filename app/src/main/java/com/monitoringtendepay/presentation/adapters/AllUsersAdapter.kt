package com.monitoringtendepay.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.monitoringtendepay.R
import com.monitoringtendepay.data.remote.dto.allusers.UserDetails

class AllUsersAdapter(private var users: List<UserDetails>) : RecyclerView.Adapter<AllUsersAdapter.UserViewHolder>() {

    fun updateData(newUsers: List<UserDetails>) {
        users = newUsers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_added_users, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val username: TextView = itemView.findViewById(R.id.username_item)
        private val createdAt: TextView = itemView.findViewById(R.id.createdAt_item)
        private val role: TextView = itemView.findViewById(R.id.role_item)
        private val status: TextView = itemView.findViewById(R.id.status_txt_item)
        private val createdBy: TextView = itemView.findViewById(R.id.createdBy_item)
        private val actions: ImageView = itemView.findViewById(R.id.actions_Image_View)

        fun bind(user: UserDetails) {
            username.text = user.username
            createdAt.text = user.createdAt
            role.text = user.role
            status.text = getStatusText(user.status)
            status.setTextColor(getStatusColor(user.status))
            createdBy.text = user.createdBy
            actions.setImageResource(R.drawable.actions)
        }

        private fun getStatusText(status: String): String {
            return when (status.lowercase()) {
                "active" -> "Active"
                "otp" -> "OTP"
                "inactive" -> "Deactivated"
                else -> "Unknown"
            }
        }

        private fun getStatusColor(status: String): Int {
            return when (status.lowercase()) {
                "active" -> itemView.context.getColor(R.color.green)
                "otp" -> itemView.context.getColor(R.color.light_blue)
                "inactive" -> itemView.context.getColor(R.color.red)
                else -> itemView.context.getColor(R.color.black)
            }
        }
    }
}