package com.monitoringtendepay.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.monitoringtendepay.R

class FailedActivity : AppCompatActivity() {

    private lateinit var closeButton: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_failed)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        closeButton = findViewById(R.id.closeButton)

        closeButton.setOnClickListener {
            val intent = Intent(this, AddUser::class.java)
            startActivity(intent)
            finish()
        }

        val responseText: TextView = findViewById(R.id.responseText)
        val message = intent.getStringExtra("responseMessage")

        message?.let {
            responseText.text = it
        }
    }
}