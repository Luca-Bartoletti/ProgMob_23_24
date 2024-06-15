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


    NavHost(navController = navController, startDestination = "loadingScreen"){
        navigation(startDestination = "logoScreen", route= "loadingScreen"){
            composable("logoScreen") { LogoScreen(navController = navController,loginViewModel)}
        }
        navigation(startDestination = "permissionsScreen", route = "onboardingRoute"){
            composable("permissionsScreen") { PermissionsScreen(navController = navController, locationViewModel = locationViewModel, loginViewModel=loginViewModel) }
        }
        navigation(startDestination = "loginScreen", route = "loginRoute"){
            composable("loginScreen") {LoginScreen(navController, loginViewModel)}
        }
        navigation(startDestination = "mainScreen", route = "mainRoute") {
            composable("mainScreen") {HomeScreen(navController, historyModelView, loginViewModel = loginViewModel, locationViewModel = locationViewModel)}
            composable("historyScreen") {HistoryScreen(navController, historyModelView, loginViewModel)}
            composable("compareScreen") {CompareScreen(navController, loginViewModel = loginViewModel, historyViewModel = historyModelView)}
        }
        navigation(startDestination = "debugScreen", route="debugRoute"){
            composable("debugScreen") { LocationReaderDebug(locationViewModel = locationViewModel)}
        }
    }
}

