package io.github.kabirnayeem99.resumade.ui.activities

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.resumade.R

@AndroidEntryPoint
class PreviewActivity : AppCompatActivity() {

    private val extraHtml: String = "html"
    private lateinit var toolbar: Toolbar
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        webView = findViewById(R.id.previewActivityWebView)
        toolbar = findViewById(R.id.previewActivityToolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Preview"

        val receivedIntent = intent
        val html = receivedIntent.getStringExtra(extraHtml)

        webView.loadDataWithBaseURL(null, html ?: "", "text/html", "utf-8", null)
    }

}
