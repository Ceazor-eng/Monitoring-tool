package com.monitoringtendepay.presentation.activities

import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.monitoringtendepay.R
import com.monitoringtendepay.domain.models.FilterUssdSessionsParams
import com.monitoringtendepay.presentation.viewmodels.FilterUssdSessionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FilterUssdSessions : AppCompatActivity() {

    private val viewModel: FilterUssdSessionsViewModel by viewModels()
    private lateinit var phoneNumberEditText: EditText
    private lateinit var sessionIdEditText: EditText
    private lateinit var startDateTextView: TextView
    private lateinit var endDateTextView: TextView
    private val STORAGE_PERMISSION_CODE = 100

    private var startDate: String = ""
    private var endDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_filter_ussd_sessions)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        sessionIdEditText = findViewById(R.id.sessionIdEditText)
        startDateTextView = findViewById(R.id.startDateTextView)
        endDateTextView = findViewById(R.id.endDateTextView)
        val generateReportButton: Button = findViewById(R.id.generateReportButton)

        startDateTextView.setOnClickListener { showDatePickerDialog(true) }
        endDateTextView.setOnClickListener { showDatePickerDialog(false) }

        generateReportButton.setOnClickListener {
            val action = "filterSessions"
            val phoneNumber = phoneNumberEditText.text.toString()
            val sessionId = sessionIdEditText.text.toString()
            val startDate = startDate
            val endDate = endDate

            viewModel.filterUssdSessions(action, phoneNumber, sessionId, startDate, endDate)
        }

        observeFilteredSessions()
    }

    private fun observeFilteredSessions() {
        viewModel.ussdSessionState.onEach { state ->
            when {
                state.isLoading -> {
                    Log.d("FilterUssdSessionsActivity", "Error: ${state.error}")
                }
                state.error.isNotEmpty() -> {
                    Log.d("FilterUssdSessionsActivity", "Error: ${state.error}")
                    Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
                }
                state.filterUssdSessions != null -> {
                    Log.d("FilterUssdSessionsActivity", "Success: ${state.filterUssdSessions}")
                    // Generate PDF report here
                    generatePdf(state.filterUssdSessions)
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun checkPermission(): Boolean {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return permission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generatePdf(filterUssdSessions: List<FilterUssdSessionsParams>) {
        if (!checkPermission()) {
            requestPermission()
            return
        }

        val externalStorageDir = Environment.getExternalStorageDirectory().absolutePath
        val directoryPath = "$externalStorageDir/Download"
        val directory = File(directoryPath)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val filePath = "$directoryPath/filtered_UssdSessions_report.pdf"
        val file = File(filePath)

        try {
            val writer = PdfWriter(FileOutputStream(file))
            val pdfDoc = com.itextpdf.kernel.pdf.PdfDocument(writer)
            val document = Document(pdfDoc)

            document.add(Paragraph("Filtered Ussd Sessions Report"))

            val table = Table(floatArrayOf(4f, 4f, 4f))
            table.addCell("Phone Number")
            table.addCell("Session ID")
            table.addCell("Session Date")

            for (item in filterUssdSessions) {
                table.addCell(item.msisdn)
                table.addCell(item.sessionId)
                table.addCell(item.sessionsDate)
            }
            document.add(table)
            document.close()

            Toast.makeText(this, "Report generated: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error generating PDF: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showDatePickerDialog(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                if (isStartDate) {
                    startDate = date
                    startDateTextView.text = date
                } else {
                    endDate = date
                    endDateTextView.text = date
                }
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}