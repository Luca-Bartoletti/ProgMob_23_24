package com.example.betterpath.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(){
    val navController  = rememberNavController()

    NavHost(navController = navController, startDestination = "loginRoute"){
        navigation(startDestination = "loginScreen", route = "loginRoute"){
            composable("loginScreen") {LoginScreen(navController)}
        }
        navigation(startDestination = "mainScreen", route = "mainRoute") {
            composable("mainScreen") {HomeScreen(navController)}
            composable("historyScreen") {HistoryScreen(navController)}
        }
    }
}