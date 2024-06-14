package com.example.betterpath.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.betterpath.viewModel.HistoryViewModel
import com.example.betterpath.viewModel.LocationViewModel
import com.example.betterpath.viewModel.LoginViewModel

@Composable
fun AppNavigation(historyModelView: HistoryViewModel, loginViewModel: LoginViewModel, locationViewModel: LocationViewModel){
    val navController  = rememberNavController()
    val isUserFirstTime by loginViewModel.isFirstTime.collectAsState(initial = true)

    NavHost(navController = navController, startDestination = "loadingScreen"){
        navigation(startDestination = "loginScreen", route = "loginRoute"){
            composable("loginScreen") {LoginScreen(navController, loginViewModel)}
            composable("permissionScreen"){PermissionsScreen(locationViewModel, navController)}
        }
        navigation(startDestination = "mainScreen", route = "mainRoute") {
            composable("mainScreen") {HomeScreen(navController, historyModelView, loginViewModel = loginViewModel)}
            composable("historyScreen") {HistoryScreen(navController, historyModelView, loginViewModel)}
            composable("compareScreen") {CompareScreen(navController, loginViewModel = loginViewModel, historyViewModel = historyModelView)}
        }
        navigation(startDestination = "permissionsScreen", route="debugRoute"){
            composable("debugScreen") { LocationReaderDebug(locationViewModel = locationViewModel)}
            composable("permissionsScreen"){ PermissionsScreen(locationViewModel = locationViewModel, navController)}
        }
        navigation(startDestination = "logoScreen", route= "loadingScreen"){
            composable("logoScreen") { LogoScreen(navController = navController, if (isUserFirstTime) "loginRoute" else "mainRoute")}
        }
    }
}

