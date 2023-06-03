package ru.rybak


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import ru.rybak.ui.theme.X10AcademwebviewTheme

class MainActivity : ComponentActivity() {
    private lateinit var appsflyerManager: AppsflyerManager
    private var isAppsflyerDataReceived = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appsflyerManager = AppsflyerManager(this)
        appsflyerManager.setConversionDataListener(object : AppsflyerManager.ConversionDataListener {
            override fun onConversionDataReceived(sub1: String, sub2: String, sub3: String, sub4: String, sub5: String) {
                isAppsflyerDataReceived = true
                setContent { MainScreen(this)}
            }
        })
        appsflyerManager.init()
    }

    @Composable
    fun MainScreen(mainActivity: AppsflyerManager.ConversionDataListener) {
        X10AcademwebviewTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                if (isAppsflyerDataReceived) {
                    Scaffold(
                        topBar = {},
                        content = { CustomWebView(mainActivity, appsflyerManager.getSubIds()); it }
                    )
                } else {
                    // Показать загрузку или другой экран ожидания
                }
            }
        }
    }

    @Composable
    fun CustomWebView(mainActivity: AppsflyerManager.ConversionDataListener, subIds: Map<String, String>) {

        val neededUrl = buildString {
            append("https://xn--80adgdap9a0aiak.xn--p1ai/?")

            for ((key, value) in subIds) {
                append(key)
                append("=")
                append(value)
                append("&")
            }
        }

        AndroidView(
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView,
                            request: WebResourceRequest
                        ): Boolean {
                            Log.d("CustomWebView", "Loading URL: ${request.url}")
                            return super.shouldOverrideUrlLoading(view, request)
                        }
                    }
                    settings.javaScriptEnabled = true
                    loadUrl(neededUrl)
                }
            },
            update = {
                Log.d("CustomWebView", "Updating URL: $neededUrl")
                it.loadUrl(neededUrl)
            },
        )
    }
}