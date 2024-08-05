package com.monitoringtendepay.presentation.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
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
    private lateinit var spinnerServiceType: Spinner
    private lateinit var spinnerStatus: Spinner
    private lateinit var editTextStartDate: TextView
    private lateinit var editTextEndDate: TextView
    private lateinit var buttonGenerateReport: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_filter_payments)
        setUpInsets()
        initializeViews()
        setupSpinners()
        setupDatePickers()
        buttonGenerateReport.setOnClickListener {
            handleGenerateReportClick()
        }
        observeFilteredPayments()
    }

    private fun setUpInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initializeViews() {
        spinnerServiceType = findViewById(R.id.spinnerServiceType)
        spinnerStatus = findViewById(R.id.spinnerStatus)
        editTextStartDate = findViewById(R.id.startDateTextView)
        editTextEndDate = findViewById(R.id.endDateTextView)
        buttonGenerateReport = findViewById(R.id.buttonGenerateReport)
    }

    private fun setupSpinners() {
        viewModel.serviceCodes.observe(this) { serviceCodes ->
            val serviceTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, serviceCodes)
            serviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerServiceType.adapter = serviceTypeAdapter
        }

        viewModel.statusCodes.observe(this) { statuses ->
            val statusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statuses)
            statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerStatus.adapter = statusAdapter
        }
    }

    private fun setupDatePickers() {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendar = Calendar.getInstance()

        val startDatePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            editTextStartDate.text = dateFormatter.format(calendar.time)
        }

        val endDatePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            editTextEndDate.text = dateFormatter.format(calendar.time)
        }

        editTextStartDate.setOnClickListener {
            DatePickerDialog(this, startDatePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        editTextEndDate.setOnClickListener {
            DatePickerDialog(this, endDatePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun handleGenerateReportClick() {
        val action = "filterPayments"
        val serviceType = spinnerServiceType.selectedItem.toString()
        val status = spinnerStatus.selectedItem.toString()
        val statusCode = mapStatusToCode(status)
        val startDate = editTextStartDate.text.toString()
        val endDate = editTextEndDate.text.toString()

        // Validate selections
        if (serviceType == "Select service type" || status == "Select status") {
            Toast.makeText(this, "Please select valid options", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.filterPayments(action, serviceType, statusCode.toString(), startDate, endDate)
    }

    private fun mapStatusToCode(status: String): Int {
        return when (status) {
            "success" -> 1
            "pending" -> 2
            "missing" -> 3
            "failed" -> 4
            else -> -1
        }
    }

    private fun observeFilteredPayments() {
        viewModel.paymentState.onEach { state ->
            when {
                state.isLoading -> {
                    Log.d("FilterPaymentsActivity", "Loading...")
                }
                state.error.isNotEmpty() -> {
                    Log.d("FilterPaymentsActivity", "Error: ${state.error}")
                    Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
                }
                state.filterPayments != null -> {
                    Log.d("FilterPaymentsActivity", "Success: ${state.filterPayments}")
                    generatePdf(state.filterPayments)
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
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

        val directoryPath = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath ?: return
        val filePath = "$directoryPath/filtered_payments_report.pdf"
        val file = File(filePath)

        try {
            val writer = PdfWriter(FileOutputStream(file))
            val pdfDoc = com.itextpdf.kernel.pdf.PdfDocument(writer)
            val document = Document(pdfDoc)

            document.add(Paragraph("Payments Report"))

            val columnWidths = floatArrayOf(2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f)
            val table = Table(columnWidths).apply {
                setWidth(100f)
            }

            val headerFont = com.itextpdf.kernel.font.PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD)
            val cellFont = com.itextpdf.kernel.font.PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA)

            val headers = listOf(
                "Service Code", "Initiator Phone", "Amount", "Internal Ref", "Mpesa Ref",
                "SalesForce Phone", "Group ID", "Recipient Name", "Session ID",
                "Payment Status", "Payment Status Message", "Transaction Date"
            )

            headers.forEach { header ->
                table.addHeaderCell(Paragraph(header).setFont(headerFont).setFontSize(10f))
            }

            filterPayments.forEach { item ->
                table.addCell(Paragraph(item.serviceCode).setFont(cellFont).setFontSize(8f))
                table.addCell(Paragraph(item.initiatorPhone).setFont(cellFont).setFontSize(8f))
                table.addCell(Paragraph(item.amount).setFont(cellFont).setFontSize(8f))
                table.addCell(Paragraph(item.internalRef).setFont(cellFont).setFontSize(8f))
                table.addCell(Paragraph(item.mpesaRef).setFont(cellFont).setFontSize(8f))
                table.addCell(Paragraph(item.salesforcePhone).setFont(cellFont).setFontSize(8f))
                table.addCell(Paragraph(item.groupId).setFont(cellFont).setFontSize(8f))
                table.addCell(Paragraph(item.initiatorName).setFont(cellFont).setFontSize(8f))
                table.addCell(Paragraph(item.sessionId).setFont(cellFont).setFontSize(8f))
                table.addCell(Paragraph(item.paymentStatus).setFont(cellFont).setFontSize(8f))
                table.addCell(Paragraph(item.paymentStatusMessage).setFont(cellFont).setFontSize(8f))
                table.addCell(Paragraph(item.transactionDate).setFont(cellFont).setFontSize(8f))
            }

            document.add(table)
            document.close()
            Toast.makeText(this, "PDF generated at $filePath", Toast.LENGTH_SHORT).show()

            // openPdf(file)
            openPdf(file)
        } catch (e: Exception) {
            Log.e("FilterPaymentsActivity", "Error generating PDF: ${e.message}")
        }
    }

    private fun openPdf(file: File) {
        val pdfUri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(pdfUri, "application/pdf")
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(intent, "Open PDF with")
        startActivity(chooser)
    }
}