package com.example.betterpath.composables

import android.app.Application
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.betterpath.foreground.ForegroundApp
import com.example.betterpath.foreground.ForegroundLocation
import com.example.betterpath.viewModel.HistoryViewModel
import com.example.betterpath.viewModel.LocationViewModel
import com.example.betterpath.viewModel.LoginViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    historyViewModel: HistoryViewModel,
    loginViewModel: LoginViewModel,
    locationViewModel: LocationViewModel
) {
    val isTracking = locationViewModel.isTracking.collectAsState(initial = false)
    val findingPosition = locationViewModel.locationData.collectAsState(null).value
    val context = LocalContext.current.applicationContext

    ScreenWithMenu(content =
    {
        Scaffold(
            topBar = { Header(navController = navController, loginViewModel = loginViewModel) },
            bottomBar = {
                Footer(
                    navController = navController,
                    historyButton = true,
                    historyViewModel = historyViewModel
                )
            },
            floatingActionButton = {
                LargeFloatingActionButton(
                    onClick = {
                        if (isTracking.value) {
                            locationViewModel.stopLocationUpdates()

                            Intent(context, ForegroundLocation::class.java).also {
                                it.action = ForegroundLocation.Actions.START.toString()
                                context.startService(it)
                            }

                        }
                        else{
                            locationViewModel.startLocationUpdates()
                            Intent(context, ForegroundLocation::class.java).also {
                                it.action = ForegroundLocation.Actions.STOP.toString()
                                context.startService(it)
                            }
                        }
                    },
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
                    if (isTracking.value)
                        findingPosition?.let {
                            if (findingPosition.isEmpty())  CircularProgressIndicator(modifier = Modifier, Color.Black)
                            else Icon(Icons.Default.Close, contentDescription = "stop track button")
                        } ?: CircularProgressIndicator(modifier = Modifier, Color.Black)
                    else
                        Icon( Icons.Default.PlayArrow, contentDescription = "play track button")
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
        ) { innerPadding -> HomeContent(innerPadding, locationViewModel) }
    }, loginViewModel = loginViewModel, navController = navController
    )


}


@Composable
fun HomeContent(innerPadding: PaddingValues, locationViewModel: LocationViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(
            modifier = Modifier
                .height(16.dp)
                .weight(0.1f)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.6f)
                .padding(horizontal = 32.dp)
        ) {
            GMaps(
                lng = (locationViewModel.maxLng + locationViewModel.maxLng)/2,
                lat = (locationViewModel.maxLat + locationViewModel.maxLat)/2
            )
        }
        Spacer(
            modifier = Modifier
                .height(16.dp)
                .weight(0.3f)
        )
    }
}



//@Preview
//@Composable
//fun HomeScreenPreview() {
//    rememberNavController()
//    HomeScreen(
//        navController = NavController(LocalContext.current),
//        loginViewModel = LoginViewModel(PreferenceRepository(LocalContext.current)),
//        historyViewModel = HistoryViewModel()
//    )
//}