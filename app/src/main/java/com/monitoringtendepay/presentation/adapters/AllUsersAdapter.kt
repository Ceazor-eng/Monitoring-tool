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
        val user = users[position]
        holder.username.text = user.username
        holder.createdAt.text = user.createdAt
        holder.role.text = user.role
        holder.status.text = user.status
        holder.createdBy.text = user.createdBy
        holder.actions.setImageResource(R.drawable.actions)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.username_item)
        val createdAt: TextView = itemView.findViewById(R.id.createdAt_item)
        val role: TextView = itemView.findViewById(R.id.role_item)
        val status: TextView = itemView.findViewById(R.id.status_txt_item)
        val createdBy: TextView = itemView.findViewById(R.id.createdBy_item)
        val actions: ImageView = itemView.findViewById(R.id.actions_Image_View)
    }
}