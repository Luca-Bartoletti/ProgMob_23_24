package com.example.betterpath.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.betterpath.viewModel.HistoryViewModel

@Composable
fun AppNavigation(historyModelView: HistoryViewModel){
    val navController  = rememberNavController()

    NavHost(navController = navController, startDestination = "loginRoute"){
        navigation(startDestination = "loginScreen", route = "loginRoute"){
            composable("loginScreen") {LoginScreen(navController)}
        }
        navigation(startDestination = "mainScreen", route = "mainRoute") {
            composable("mainScreen") {HomeScreen(navController, historyModelView)}
            composable("historyScreen") {HistoryScreen(navController, historyModelView)}
        }
    }
}

