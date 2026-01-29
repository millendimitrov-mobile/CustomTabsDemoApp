package com.milen.customtabsdemoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.milen.customtabsdemoapp.ui.screens.HomeScreen
import com.milen.customtabsdemoapp.ui.screens.SettingsScreen
import com.milen.customtabsdemoapp.ui.screens.WebViewScreen
import com.milen.customtabsdemoapp.ui.viewmodel.AppViewModel

object Routes {
    const val HOME = "home"
    const val SETTINGS = "settings"
    const val WEBVIEW = "webview"
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: AppViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToSettings = { navController.navigate(Routes.SETTINGS) },
                onNavigateToWebView = { navController.navigate(Routes.WEBVIEW) }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.WEBVIEW) {
            WebViewScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
