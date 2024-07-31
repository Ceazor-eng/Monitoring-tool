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

class SuccessActivity : AppCompatActivity() {

    private lateinit var closeBtn: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_success)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        closeBtn = findViewById(R.id.closeBtn)

        closeBtn.setOnClickListener {
            val intent = Intent(this, AddUser::class.java)
            startActivity(intent)
            finish()
        }

        val responseText: TextView = findViewById(R.id.responseSuccessText)
        val message = intent.getStringExtra("responseMessage")

        message?.let {
            responseText.text = it
        }
    }
}