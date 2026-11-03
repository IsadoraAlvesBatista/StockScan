package com.posto.stockscan.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.posto.stockscan.AppContainer
import com.posto.stockscan.ui.screens.home.HomeScreen
import com.posto.stockscan.ui.screens.login.LoginScreen
import com.posto.stockscan.ui.screens.login.LoginViewModel
import com.posto.stockscan.ui.screens.scanner.ScannerScreen
import com.posto.stockscan.ui.screens.scanner.ScannerViewModel
import com.posto.stockscan.ui.screens.setup.SetupScreen
import com.posto.stockscan.ui.screens.setup.SetupViewModel
import com.posto.stockscan.ui.screens.users.UsersScreen
import com.posto.stockscan.ui.screens.users.UsersViewModel

private fun NavHostController.goToClearingBackStack(route: String) {
    navigate(route) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

@Composable
fun StockScanNavHost(container: AppContainer) {
    val navController = rememberNavController()
    // Primeiro arranque (sem utilizadores) → criar o Administrador; senão → login.
    // rememberSaveable: o valor sobrevive à rotação da tela.
    var startRoute by rememberSaveable { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        if (startRoute == null) startRoute = if (container.userRepository.hasAnyUser()) Routes.LOGIN else Routes.SETUP
    }

    val start = startRoute
    if (start == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }

    NavHost(navController = navController, startDestination = start) {
        composable(Routes.SETUP) {
            val vm = viewModel { SetupViewModel(container.userRepository, container.session) }
            SetupScreen(vm, onDone = { navController.goToClearingBackStack(Routes.HOME) })
        }
        composable(Routes.LOGIN) {
            val vm = viewModel { LoginViewModel(container.userRepository, container.session) }
            LoginScreen(vm, onLoggedIn = { navController.goToClearingBackStack(Routes.HOME) })
        }
        composable(Routes.HOME) {
            HomeScreen(
                session = container.session,
                onOpenUsers = { navController.navigate(Routes.USERS) },
                onOpenScanner = { navController.navigate(Routes.SCANNER) },
                onLogout = {
                    container.session.logout()
                    navController.goToClearingBackStack(Routes.LOGIN)
                },
            )
        }
        composable(Routes.USERS) {
            // Só o Administrador gere utilizadores.
            if (!container.session.isAdmin) {
                LaunchedEffect(Unit) { navController.popBackStack() }
                return@composable
            }
            val vm = viewModel { UsersViewModel(container.userRepository, container.session) }
            UsersScreen(vm, onBack = { navController.popBackStack() })
        }
        composable(Routes.SCANNER) {
            val vm = viewModel { ScannerViewModel(container.productRepository) }
            ScannerScreen(vm, onBack = { navController.popBackStack() })
        }
    }
}
