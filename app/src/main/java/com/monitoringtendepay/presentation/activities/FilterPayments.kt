package com.monitoringtendepay.presentation.activities

import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
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
import com.monitoringtendepay.R
import com.monitoringtendepay.domain.models.PaymentsFilterParams
import com.monitoringtendepay.presentation.viewmodels.FilterPaymentsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FilterPayments : AppCompatActivity() {

    private val viewModel: FilterPaymentsViewModel by viewModels()
    private val STORAGE_PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_filter_payments)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinnerServiceType: Spinner = findViewById(R.id.spinnerServiceType)
        val spinnerStatus: Spinner = findViewById(R.id.spinnerStatus)
        val editTextStartDate: EditText = findViewById(R.id.editTextStartDate)
        val editTextEndDate: EditText = findViewById(R.id.editTextEndDate)
        val buttonGenerateReport: Button = findViewById(R.id.buttonGenerateReport)

        // Populate Service Type Spinner
        val serviceTypes = listOf("mpesa", "bank")
        val serviceTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, serviceTypes)
        serviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerServiceType.adapter = serviceTypeAdapter

        // Populate Status Spinner
        val statuses = listOf("1", "2", "3", "4")
        val statusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statuses)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = statusAdapter

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendar = Calendar.getInstance()

        val startDatePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            editTextStartDate.setText(dateFormatter.format(calendar.time))
        }

        val endDatePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            editTextEndDate.setText(dateFormatter.format(calendar.time))
        }

        editTextStartDate.setOnClickListener {
            DatePickerDialog(this, startDatePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        editTextEndDate.setOnClickListener {
            DatePickerDialog(this, endDatePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        buttonGenerateReport.setOnClickListener {
            val action = "filterPayments"
            val serviceType = spinnerServiceType.selectedItem.toString()
            val status = spinnerStatus.selectedItem.toString()
            val startDate = editTextStartDate.text.toString()
            val endDate = editTextEndDate.text.toString()

            viewModel.filterPayments(action, serviceType, status, startDate, endDate)
        }

        observeFilteredPayments()
    }

    private fun observeFilteredPayments() {
        viewModel.paymentState.onEach { state ->
            when {
                state.isLoading -> {
                    Log.d("FilterPaymentsActivity", "Loading...")
                    //   Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                }
                state.error.isNotEmpty() -> {
                    Log.d("FilterPaymentsActivity", "Error: ${state.error}")
                    Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
                }
                state.filterPayments != null -> {
                    Log.d("FilterPaymentsActivity", "Success: ${state.filterPayments}")
                    // Generate PDF report here
                    generatePdf(state.filterPayments)
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

    private fun generatePdf(filterPayments: List<PaymentsFilterParams>) {
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

        val filePath = "$directoryPath/filtered_payments_report.pdf"
        val file = File(filePath)

        try {
            val writer = PdfWriter(FileOutputStream(file))
            val pdfDoc = com.itextpdf.kernel.pdf.PdfDocument(writer)
            val document = Document(pdfDoc)

            document.add(Paragraph("Filtered Payments Report"))

            filterPayments.forEach { payment ->
                document.add(Paragraph("Service Code: ${payment.serviceCode}"))
                document.add(Paragraph("Payment Status: ${payment.paymentStatus}"))
                document.add(Paragraph("Transaction Date: ${payment.transactionDate}"))
                document.add(Paragraph("-----"))
            }
            document.close()
            Toast.makeText(this, "PDF Report Generated at $filePath", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error generating PDF: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }

        /*
                val path = getExternalFilesDir(null)?.absolutePath + "/report.pdf"
        val writer = PdfWriter(path)
        val pdfDoc = com.itextpdf.kernel.pdf.PdfDocument(writer)
        val document = Document(pdfDoc)

        document.add(Paragraph("Filtered Payments Report"))

        filterPayments.forEach { payment ->
            document.add(Paragraph("Service Code: ${payment.serviceCode}"))
            document.add(Paragraph("Payment Status: ${payment.paymentStatus}"))
            document.add(Paragraph("Transaction Date: ${payment.transactionDate}"))
            document.add(Paragraph("-----"))
        }

        document.close()

        Toast.makeText(this, "PDF Report Generated at $path", Toast.LENGTH_LONG).show()

         */
    }
}