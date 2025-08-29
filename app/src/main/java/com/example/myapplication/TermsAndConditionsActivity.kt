package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.widget.Toolbar

class TermsAndConditionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)
        
        // Set up toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbarTermsConditions)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        
        // Load the terms and conditions content into the WebView
        val webView = findViewById<WebView>(R.id.webViewTermsConditions)
        
        // Configure WebView settings for better readability
        webView.settings.apply {
            javaScriptEnabled = false
            builtInZoomControls = true
            displayZoomControls = false
            setSupportZoom(true)
            defaultFontSize = 16
        }
        
        val termsContent = getString(R.string.terms_and_conditions_content)
        
        // Load the HTML content with better encoding support
        webView.loadDataWithBaseURL(
            null,
            "<html><head>" +
            "<meta charset=\"UTF-8\">" +
            "<style type='text/css'>" +
            "body { padding: 16px; font-family: sans-serif; line-height: 1.6; color: #333; }" +
            "h1 { color: #4285F4; font-size: 24px; margin-top: 24px; margin-bottom: 16px; }" +
            "h2 { color: #4285F4; font-size: 20px; margin-top: 24px; margin-bottom: 12px; }" +
            "p { margin-bottom: 16px; }" +
            "ul { margin-bottom: 16px; padding-left: 24px; }" +
            "li { margin-bottom: 8px; }" +
            "</style></head><body>" +
            termsContent +
            "</body></html>",
            "text/html",
            "UTF-8",
            null
        )
    }
    
    // Handle the back button in the toolbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
