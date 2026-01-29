package com.milen.customtabsdemoapp.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.milen.customtabsdemoapp.R
import com.milen.customtabsdemoapp.ui.viewmodel.AppViewModel

private const val TAG = "WebViewScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit
) {
    val loginUrl by viewModel.loginUrl.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    var loadingProgress by remember { mutableIntStateOf(0) }
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.webview_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.content_desc_back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { webViewInstance?.reload() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.content_desc_reload)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AndroidView(
                factory = { context ->
                    FrameLayout(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        val webView = createAutofillFriendlyWebView(context).apply {
                            layoutParams = FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT
                            )
                        }
                        webViewInstance = webView

                        webView.webViewClient = object : WebViewClient() {
                            override fun onPageStarted(
                                view: WebView?,
                                url: String?,
                                favicon: Bitmap?
                            ) {
                                super.onPageStarted(view, url, favicon)
                                isLoading = true
                                Log.d(TAG, "onPageStarted: $url")
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading = false
                                view?.scrollTo(0, 0)
                                Log.d(TAG, "onPageFinished: $url")
                            }
                        }

                        webView.webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                super.onProgressChanged(view, newProgress)
                                loadingProgress = newProgress
                                if (newProgress == 100) {
                                    isLoading = false
                                }
                            }
                        }

                        addView(webView)
                        webView.loadUrl(loginUrl)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            if (isLoading) {
                LinearProgressIndicator(
                    progress = { loadingProgress / 100f },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun createAutofillFriendlyWebView(context: android.content.Context): WebView {
    return WebView(context).apply {
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            useWideViewPort = false
            loadWithOverviewMode = false
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
        }

        setInitialScale(0)
        isFocusable = true
        isFocusableInTouchMode = true
        importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_YES
        scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

        Log.d(TAG, "WebView configured with autofill-friendly settings")
    }
}
