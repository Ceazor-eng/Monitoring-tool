package com.monitoringtendepay.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.monitoringtendepay.R
import com.monitoringtendepay.data.remote.dto.allusers.UserDetails
import com.monitoringtendepay.presentation.viewmodels.UserActionsViewModel

class AllUsersAdapter(
    private var users: List<UserDetails>,
    private val userActionsViewModel: UserActionsViewModel
) : RecyclerView.Adapter<AllUsersAdapter.UserViewHolder>() {

    private var originalUsers: List<UserDetails> = users

    fun updateData(newUsers: List<UserDetails>) {
        originalUsers = newUsers
        users = newUsers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.users, parent, false)
        return UserViewHolder(view, userActionsViewModel)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class UserViewHolder(itemView: View, private val userActionsViewModel: UserActionsViewModel) : RecyclerView.ViewHolder(itemView) {

        private val username: TextView = itemView.findViewById(R.id.username_item)
        private val role: TextView = itemView.findViewById(R.id.role_item)
        private val status: TextView = itemView.findViewById(R.id.status_txt_item)
        private val makeAdmin: TextView = itemView.findViewById(R.id.Make_Admin_txt)
        private val activate: TextView = itemView.findViewById(R.id.Activate_txt)
        private val deactivate: TextView = itemView.findViewById(R.id.Deactivate_txt)
        private val resendOtp: TextView = itemView.findViewById(R.id.Resend_OTP_txt)

        fun bind(user: UserDetails) {
            username.text = user.username.capitalizeFirstLetter()
            role.text = user.role.capitalizeFirstLetter()
            status.text = getStatusText(user.status).capitalizeFirstLetter()
            status.setTextColor(getStatusColor(user.status))
            makeAdmin.setOnClickListener {
                userActionsViewModel.makeUserAdmin("makeAdmin", user.username)
            }

            activate.setOnClickListener {
                userActionsViewModel.activateUser("activateUser", user.username)
            }

            deactivate.setOnClickListener {
                userActionsViewModel.deactivateUser("disableUser", user.username)
            }

            resendOtp.setOnClickListener {
                userActionsViewModel.resendOtp("regenerateOtp", user.username)
            }
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

        private fun String.capitalizeFirstLetter(): String {
            return this.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
    }
}