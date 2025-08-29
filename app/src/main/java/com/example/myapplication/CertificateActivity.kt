package com.example.myapplication

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CertificateActivity : AppCompatActivity() {
    
    private lateinit var tvUserName: TextView
    private lateinit var tvCourseName: TextView
    private lateinit var tvCompletionDate: TextView
    private lateinit var btnSaveCertificate: Button
    private lateinit var btnShare: Button
    private lateinit var certificateContainer: View
    private lateinit var progressBar: ProgressBar
    
    private val PERMISSION_REQUEST_CODE = 100
    private var certificatePdfFile: File? = null
    private var userName: String = ""
    private var courseName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certificate)

        // Initialize views
        tvUserName = findViewById(R.id.tvUserName)
        tvCourseName = findViewById(R.id.tvCourseName)
        tvCompletionDate = findViewById(R.id.tvCompletionDate)
        btnSaveCertificate = findViewById(R.id.btnSaveCertificate)
        btnShare = findViewById(R.id.btnShare)
        certificateContainer = findViewById(R.id.certificateContainer)
        
        // Add progress bar to the layout if it doesn't exist
        progressBar = findViewById(R.id.progressBar) ?: ProgressBar(this).apply {
            visibility = View.GONE
        }

        // Get data from intent
        userName = intent.getStringExtra("userName") ?: "Student"
        courseName = intent.getStringExtra("courseName") ?: "Course"
        val score = intent.getIntExtra("score", 0)
        val maxScore = intent.getIntExtra("maxScore", 0)
        
        // Format current date
        val currentDate = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())

        // Set data to views
        tvUserName.text = userName
        tvCourseName.text = courseName
        tvCompletionDate.text = "Completed on $currentDate"

        // Save certificate as PDF
        btnSaveCertificate.setOnClickListener {
            if (checkPermissions()) {
                showDownloadOptions()
            } else {
                requestPermissions()
            }
        }

        // Share certificate
        btnShare.setOnClickListener {
            if (certificatePdfFile != null && certificatePdfFile!!.exists()) {
                shareCertificate()
            } else {
                // Generate the PDF first, then share it
                if (checkPermissions()) {
                    saveCertificateAsPdf(userName, courseName) { success ->
                        if (success) {
                            shareCertificate()
                        }
                    }
                } else {
                    requestPermissions()
                }
            }
        }
    }
    
    private fun checkPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ uses scoped storage, no need for storage permission
            true
        } else {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
    
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showDownloadOptions()
            } else {
                Toast.makeText(
                    this,
                    "Permission denied. Cannot save certificate.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun showDownloadOptions() {
        val options = arrayOf("Save to Downloads", "Save and Open")
        
        AlertDialog.Builder(this)
            .setTitle("Download Certificate")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> saveCertificateAsPdf(userName, courseName) { success ->
                        if (success) {
                            Toast.makeText(
                                this,
                                "Certificate saved to Downloads folder",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    1 -> saveCertificateAsPdf(userName, courseName) { success ->
                        if (success) {
                            openPdf()
                        }
                    }
                }
            }
            .show()
    }

    private fun saveCertificateAsPdf(userName: String, courseName: String, callback: ((Boolean) -> Unit)? = null) {
        // Show progress
        progressBar.visibility = View.VISIBLE
        
        // Create a bitmap from the certificate view with high quality
        val bitmap = getBitmapFromView(certificateContainer)
        
        Thread {
            try {
                // Create a PDF document
                val document = PdfDocument()
                
                // Create a page
                val pageInfo = PdfDocument.PageInfo.Builder(
                    bitmap.width, 
                    bitmap.height, 
                    1
                ).create()
                
                val page = document.startPage(pageInfo)
                
                // Draw the bitmap on the page with high quality
                val canvas = page.canvas
                val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
                canvas.drawBitmap(bitmap, 0f, 0f, paint)
                
                // Add footer text
                val footerPaint = Paint().apply {
                    color = Color.GRAY
                    textSize = 12f
                }
                canvas.drawText("Generated by EduApp on ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())}", 
                    20f, pageInfo.pageHeight - 20f, footerPaint)
                
                // Finish the page
                document.finishPage(page)
                
                // Create a file for the PDF in Downloads folder
                val fileName = "${userName}_${courseName}_Certificate_${System.currentTimeMillis()}.pdf".replace(" ", "_")
                
                // For Android 10+ (Q) and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Save to app-specific external storage first
                    val directory = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Certificates")
                    if (!directory.exists()) {
                        directory.mkdirs()
                    }
                    
                    certificatePdfFile = File(directory, fileName)
                    val outputStream = FileOutputStream(certificatePdfFile)
                    document.writeTo(outputStream)
                    outputStream.flush()
                    outputStream.close()
                    
                    // Then use Download Manager to make it accessible
                    val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val contentUri = FileProvider.getUriForFile(
                        this,
                        "${packageName}.provider",
                        certificatePdfFile!!
                    )
                    
                    val request = DownloadManager.Request(Uri.parse(contentUri.toString()))
                        .setTitle("Course Certificate")
                        .setDescription("Downloading your certificate for $courseName")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                    
                    downloadManager.enqueue(request)
                } else {
                    // For older Android versions
                    val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Certificates")
                    if (!directory.exists()) {
                        directory.mkdirs()
                    }
                    
                    certificatePdfFile = File(directory, fileName)
                    val outputStream = FileOutputStream(certificatePdfFile)
                    document.writeTo(outputStream)
                    outputStream.flush()
                    outputStream.close()
                }
                
                // Close the document
                document.close()
                
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this, 
                        "Certificate saved to Downloads folder", 
                        Toast.LENGTH_LONG
                    ).show()
                    
                    callback?.invoke(true)
                }
                
            } catch (e: Exception) {
                e.printStackTrace()
                
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this, 
                        "Failed to save certificate: ${e.message}", 
                        Toast.LENGTH_SHORT
                    ).show()
                    
                    callback?.invoke(false)
                }
            }
        }.start()
    }
    
    private fun getBitmapFromView(view: View): Bitmap {
        // Create a bitmap with high quality
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        
        // Draw background if needed
        canvas.drawColor(Color.WHITE)
        
        // Draw the view
        view.draw(canvas)
        
        return bitmap
    }
    
    private fun openPdf() {
        if (certificatePdfFile != null && certificatePdfFile!!.exists()) {
            try {
                val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    FileProvider.getUriForFile(this, "${packageName}.provider", certificatePdfFile!!)
                } else {
                    Uri.fromFile(certificatePdfFile)
                }
                
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/pdf")
                    flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "No PDF viewer app found. Please install one.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun shareCertificate() {
        if (certificatePdfFile != null && certificatePdfFile!!.exists()) {
            try {
                val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    FileProvider.getUriForFile(this, "${packageName}.provider", certificatePdfFile!!)
                } else {
                    Uri.fromFile(certificatePdfFile)
                }
                
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    putExtra(Intent.EXTRA_SUBJECT, "Course Certificate: $courseName")
                    putExtra(Intent.EXTRA_TEXT, "I've completed the course \"$courseName\" and received this certificate!")
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                
                startActivity(Intent.createChooser(shareIntent, "Share Certificate"))
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "Failed to share certificate: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
