package com.example.betterpath.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.betterpath.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Composable
fun LogoScreen(navController : NavController, nextRoute : String? = null){
    nextRoute?.let {
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            delay(1500)
            navController.navigate(nextRoute)
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

suspend fun clock():Boolean{
    delay(2000)
    return true
}

@Preview(showBackground = true)
@Composable
fun LogoScreenPreview() {
    val context = LocalContext.current
    LogoScreen(navController = NavController(context))
}