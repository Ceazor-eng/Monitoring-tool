package com.monitoringtendepay.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.monitoringtendepay.R
import com.monitoringtendepay.core.common.ConfirmationDialog
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
        private val makeAdmin: RelativeLayout = itemView.findViewById(R.id.Make_Admin_txtlyt)
        private val activate: RelativeLayout = itemView.findViewById(R.id.Activate_txtlyt)
        private val deactivate: RelativeLayout = itemView.findViewById(R.id.Deactivate_txtlyt)
        private val resendOtp: RelativeLayout = itemView.findViewById(R.id.Resend_OTP_txtlyt)
        private val usernameInitial: TextView = itemView.findViewById(R.id.username_initial)

        fun bind(user: UserDetails) {
            username.text = user.username.capitalizeFirstLetter()
            role.text = user.role.capitalizeFirstLetter()
            status.text = getStatusText(user.status).capitalizeFirstLetter()
            status.setTextColor(getStatusColor(user.status))

            // Set the initial of the username
            usernameInitial.text = user.username.firstOrNull()?.uppercase() ?: ""

            val isAdmin = user.role.equals("admin", ignoreCase = true)
            val isUser = user.role.equals("user", ignoreCase = true)
            val isOtp = user.status.equals("otp", ignoreCase = true)
            val isActive = user.status.equals("active", ignoreCase = true)
            val isInactive = user.status.equals("inactive", ignoreCase = true)

            when (user.status) {
                "active" -> {
                    makeAdmin.visibility = if (isUser) View.VISIBLE else View.GONE
                    resendOtp.visibility = View.VISIBLE
                    activate.visibility = View.GONE
                    deactivate.visibility = View.VISIBLE
                }
                "inactive" -> {
                    makeAdmin.visibility = View.GONE
                    resendOtp.visibility = View.GONE
                    activate.visibility = if (isInactive) View.VISIBLE else View.GONE
                    deactivate.visibility = View.GONE
                }
                "OTP" -> {
                    makeAdmin.visibility = View.GONE
                    resendOtp.visibility = View.VISIBLE
                    activate.visibility = View.GONE
                    deactivate.visibility = View.GONE
                }
                else -> {
                    makeAdmin.visibility = View.GONE
                    resendOtp.visibility = View.GONE
                    activate.visibility = View.GONE
                    deactivate.visibility = View.GONE
                }
            }

            makeAdmin.setOnClickListener {
                showConfirmationDialog(
                    "Make Admin",
                    "Are you sure you want to make ${user.username} an admin?",
                    "Yes",
                    "No"
                ) {
                    userActionsViewModel.makeUserAdmin("makeAdmin", user.username)
                }
            }

            activate.setOnClickListener {
                showConfirmationDialog(
                    "Activate User",
                    "Are you sure you want to activate ${user.username}?",
                    "Yes",
                    "No"
                ) {
                    userActionsViewModel.activateUser("activateUser", user.username)
                }
            }

            deactivate.setOnClickListener {
                showConfirmationDialog(
                    "Deactivate User",
                    "Are you sure you want to deactivate ${user.username}?",
                    "Yes",
                    "No"
                ) {
                    userActionsViewModel.deactivateUser("disableUser", user.username)
                }
            }

            resendOtp.setOnClickListener {
                showConfirmationDialog(
                    "Resend OTP",
                    "Are you sure you want to resend OTP to ${user.username}?",
                    "Yes",
                    "No"
                ) {
                    userActionsViewModel.resendOtp("regenerateOtp", user.username)
                }
            }
        }

        private fun showConfirmationDialog(title: String, message: String, positiveButtonText: String, negativeButtonText: String, onPositiveClick: () -> Unit) {
            ConfirmationDialog.show(itemView.context, title, message, positiveButtonText, negativeButtonText, onPositiveClick)
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