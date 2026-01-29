package com.milen.customtabsdemoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.milen.customtabsdemoapp.data.UrlStore
import com.milen.customtabsdemoapp.ui.navigation.AppNavigation
import com.milen.customtabsdemoapp.ui.theme.CustomTabsDemoAppTheme
import com.milen.customtabsdemoapp.ui.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val urlStore = UrlStore(applicationContext)

        setContent {
            CustomTabsDemoAppTheme {
                val navController = rememberNavController()
                val viewModel = remember { AppViewModel(urlStore) }

                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}