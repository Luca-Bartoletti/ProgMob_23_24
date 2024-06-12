package com.example.betterpath.composables

import androidx.activity.viewModels
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.betterpath.R
import com.example.betterpath.repository.PreferenceRepository
import com.example.betterpath.viewModel.HistoryViewModel
import com.example.betterpath.viewModel.LoginViewModel

@Composable
fun CompareScreen(navController: NavController, loginViewModel: LoginViewModel, historyViewModel: HistoryViewModel){
    ScreenWithMenu(content = {
        Scaffold(
            topBar = { Header(navController = navController, loginViewModel = loginViewModel) },
            bottomBar = { Spacer(modifier = Modifier.height(32.dp)) },
        ) { innerPadding -> CompareContent(innerPadding, historyViewModel = historyViewModel) }
    }, navController = navController, loginViewModel = loginViewModel)
}

@Composable
fun CompareContent(innerPadding : PaddingValues, historyViewModel: HistoryViewModel){
    val differenceValue = 0.8f
    val path1 = historyViewModel.fetchFirstPath()
    val path2 = historyViewModel.fetchSecondPath()
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

            // Percorso 1
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
                        contentDescription = "color of Path 1",
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                        Text(text = stringResource(R.string.path_date) + " : " + path1?.date)
                }
                Row (
                    Modifier
                        .fillMaxSize()
                        .weight(0.5f))
                {
                    Text(modifier = Modifier.padding(horizontal = 4.dp),
                        text = stringResource(R.string.distance) + " : " + path1?.distance
                    )
                }

            }
            Spacer(modifier = Modifier.weight(0.05f))

            // Percorso 2
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
                    Text(text =stringResource(R.string.path_date) + " : " + path2?.date)
                }
                Row (
                    Modifier
                        .fillMaxSize()
                        .weight(0.5f))
                {
                    Text(modifier = Modifier.padding(horizontal = 4.dp),
                        text = stringResource(R.string.distance) + " : " + path2?.distance)
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
    CompareScreen(navController = navController, loginViewModel = LoginViewModel(PreferenceRepository(
        LocalContext.current)), historyViewModel = HistoryViewModel())
}