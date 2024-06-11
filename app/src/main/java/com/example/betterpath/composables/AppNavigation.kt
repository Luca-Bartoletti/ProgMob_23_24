package com.example.betterpath.composables

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.betterpath.viewModel.HistoryViewModel
import com.example.betterpath.viewModel.LoginViewModel

@Composable
fun AppNavigation(historyModelView: HistoryViewModel, loginViewModel: LoginViewModel){
    val navController  = rememberNavController()

    NavHost(navController = navController, startDestination = "loginRoute"){
        navigation(startDestination = "loginScreen", route = "loginRoute"){
            composable("loginScreen") {LoginScreen(navController, loginViewModel)}
        }
        navigation(startDestination = "mainScreen", route = "mainRoute") {
            composable("mainScreen") {HomeScreen(navController, historyModelView, loginViewModel = loginViewModel)}
            composable("historyScreen") {HistoryScreen(navController, historyModelView, loginViewModel)}
            composable("compareScreen") {CompareScreen(navController, loginViewModel = loginViewModel)}
        }
    }
}

