package com.example.betterpath.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.betterpath.viewModel.LoginViewModel

@Composable
fun ScreenWithMenu(
    content: @Composable () -> Unit, navController: NavController,
    loginViewModel: LoginViewModel
){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        content()

        AnimatedVisibility(
            visible = loginViewModel.isMenuOpen.value,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth }
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth }
            )
        ) {
            MenuBar(viewModel = loginViewModel, navController = navController)
        }

    }
}
