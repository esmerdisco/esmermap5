package com.esmermap.app

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webview)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadsImagesAutomatically = true
            mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            setSupportZoom(false)
            builtInZoomControls = false
            displayZoomControls = false
        }

        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val uri = request?.url ?: return false
                return handleCustomSchemes(uri)
            }

            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url.isNullOrBlank()) return false
                return handleCustomSchemes(Uri.parse(url))
            }

            private fun handleCustomSchemes(uri: Uri): Boolean {
                return when (uri.scheme?.lowercase()) {
                    "tel" -> { safeStart(Intent(Intent.ACTION_DIAL, uri)); true }
                    "mailto" -> { safeStart(Intent(Intent.ACTION_SENDTO, uri)); true }
                    "sms", "smsto" -> { safeStart(Intent(Intent.ACTION_SENDTO, uri)); true }
                    "geo" -> { safeStart(Intent(Intent.ACTION_VIEW, uri)); true }
                    else -> false
                }
            }

            private fun safeStart(intent: Intent) {
                try {
                    startActivity(intent)
                } catch (_: ActivityNotFoundException) {
                    Toast.makeText(this@MainActivity, "No app found to handle this action", Toast.LENGTH_SHORT).show()
                }
            }
        }

        webView.loadUrl("https://esmerdis.com/location/4")
    }

    override fun onBackPressed() {
        if (this::webView.isInitialized && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
