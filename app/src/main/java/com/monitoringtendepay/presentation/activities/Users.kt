package com.monitoringtendepay.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.monitoringtendepay.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Users : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addUserBtn = view.findViewById<Button>(R.id.AddUserBtn)

        addUserBtn.setOnClickListener {
            val intent = Intent(requireContext(), AddUser::class.java)
            startActivity(intent)
        }
    }
}