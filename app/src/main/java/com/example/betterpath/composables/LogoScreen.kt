package com.example.betterpath.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.betterpath.R
import com.example.betterpath.viewModel.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun LogoScreen(navController : NavController,loginViewModel: LoginViewModel? = null){

    loginViewModel?.let {
        val isUserFirstTime by loginViewModel.isFirstTime.collectAsState(initial = true)
        LaunchedEffect(Unit) {
            delay(1500)
            navController.navigate(if (isUserFirstTime) "onboardingRoute" else "mainRoute")
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(64.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircleImage(
            resource = R.drawable.logo_ia,
            contentDescription = LocalContext.current.getString(R.string.application_logo),
            onClickAction = {}
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LogoScreenPreview() {
    val context = LocalContext.current
    LogoScreen(navController = NavController(context))
}