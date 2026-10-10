package com.example.menuwebview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    // URLs each menu item points to
    private val homeUrl = "https://www.google.com"
    private val wikiUrl = "https://www.wikipedia.org"
    private val githubUrl = "https://www.github.com"

    // Local HTML shown by the "My Info" menu item (no internet needed)
    private val myInfoHtml = """
        <html>
        <body style='font-family:sans-serif; text-align:center; margin-top:60px;'>
            <h2>Experiment 8 - Menu &amp; WebView</h2>
            <p><b>Name:</b> Shubham Shivaji Kondikire</p>
            <p><b>USN:</b> 25MCAR0102</p>
            <p><b>Course:</b> MCA, Jain (Deemed-to-be University)</p>
        </body>
        </html>
    """.trimIndent()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)

        // Basic WebView setup
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        // Keep navigation inside the WebView (don't open external browser)
        webView.webViewClient = WebViewClient()

        // Drive the progress bar from page-load events
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
                progressBar.visibility = if (newProgress < 100) ProgressBar.VISIBLE else ProgressBar.GONE
            }
        }

        // Load the home page first
        webView.loadUrl(homeUrl)
    }

    // Inflate the options menu (defined in res/menu/main_menu.xml)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Handle menu item taps
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_home -> {
                webView.loadUrl(homeUrl)
                true
            }
            R.id.menu_wikipedia -> {
                webView.loadUrl(wikiUrl)
                true
            }
            R.id.menu_github -> {
                webView.loadUrl(githubUrl)
                true
            }
            R.id.menu_myinfo -> {
                webView.loadData(myInfoHtml, "text/html", "UTF-8")
                true
            }
            R.id.menu_refresh -> {
                webView.reload()
                Toast.makeText(this, "Page refreshed", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_exit -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Back button navigates WebView history instead of closing the app
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
