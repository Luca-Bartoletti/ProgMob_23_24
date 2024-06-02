package com.example.betterpath.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController? = null){
    var isTracking by remember { mutableIntStateOf(0) }
    Scaffold(
        topBar = { Header() },
        bottomBar = { Footer(navController = navController, homeButton = false) },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = {isTracking = (isTracking+1)%2},
                modifier = Modifier
                    .padding(bottom = 16.dp),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                if (isTracking == 0) Icon(Icons.Default.PlayArrow, contentDescription = "play track button")
                else Icon(Icons.Default.Close, contentDescription = "stop track button")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,

        ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier
                .height(16.dp)
                .weight(0.1f))
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.6f)
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    text = "Content",
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                )
            }
            Spacer(modifier = Modifier
                .height(16.dp)
                .weight(0.3f))
        }
    }

}

@Composable
fun Content(){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface)
        ){
            Text(
                text = "Content"
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}