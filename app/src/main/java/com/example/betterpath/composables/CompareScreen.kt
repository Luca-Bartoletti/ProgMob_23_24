package com.example.betterpath.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.betterpath.viewModel.LoginViewModel

@Composable
fun CompareScreen(navController: NavController, loginViewModel: LoginViewModel){
    Scaffold(
        topBar = { Header(navController = navController, loginViewModel = loginViewModel) },
        bottomBar = { Spacer(modifier = Modifier.height(32.dp)) },
        ) { innerPadding ->
        CompareContent(innerPadding)
    }
}

@Composable
fun CompareContent(innerPadding : PaddingValues, differenceValue:Float = 0.8f){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center
    ){
        AnimatedLine(differenceValue)

        AnimatedText(differenceValue)

        Card(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.5f)
                .padding(vertical = 16.dp),
        ){
            Text(text = "MAPPA")
        }
        // informazioni sui percorsi comparati
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier
                .weight(0.45f)
                .border(1.dp, Color.Black)
            ){
                Row (
                    Modifier
                        .fillMaxWidth()
                        .weight(0.5f),
                    verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Account image",
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                        Text(text = "Data percorso 1")
                }
                Row (
                    Modifier
                        .fillMaxSize()
                        .weight(0.5f))
                {
                    Text(modifier = Modifier.padding(horizontal = 4.dp),
                        text = "Distanza percorsa: 5Km")
                }

            }
            Spacer(modifier = Modifier.weight(0.05f))
            Column(modifier = Modifier
                .weight(0.45f)
                .border(1.dp, Color.Black)
            ){
                Row (
                    Modifier
                        .fillMaxWidth()
                        .weight(0.5f),
                    verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Account image",
                        tint = MaterialTheme.colorScheme.surface,
                    )
                    Text(text = "Data percorso 2")
                }
                Row (
                    Modifier
                        .fillMaxSize()
                        .weight(0.5f))
                {
                    Text(modifier = Modifier.padding(horizontal = 4.dp),
                        text = "Distanza percorsa: 7Km")
                }
            }

        }
        //Spacer dal fondo dello schermo
        Row(modifier = Modifier.weight(0.1f)){
            Spacer(modifier = Modifier.fillMaxWidth())
        }
    }
    
}

@Preview
@Composable
fun CompareScreenPreview() {
    val navController  = rememberNavController()
    CompareScreen(navController = navController, loginViewModel = LoginViewModel())
}