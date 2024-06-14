package com.example.betterpath.composables

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.betterpath.viewModel.LocationViewModel

@Composable
fun PermissionsScreen(locationViewModel: LocationViewModel, navController: NavController){
    //val context = LocalContext.current
    var foreGroundPermissionChecked by remember { mutableStateOf(false) }
    var backGroundPermissionChecked by remember { mutableStateOf(false) }

    val foreGroundPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted : Boolean -> locationViewModel.updateForeGroundPermissionStatus(isGranted) }

    val backGroundPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted : Boolean -> locationViewModel.updateBackGroundPermissionStatus(isGranted) }

    if(foreGroundPermissionChecked && backGroundPermissionChecked)
        navController.navigate("debugScreen")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Text(text = "Location Permissions")
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ){
            Text(text = "Foreground Permissions")
            Checkbox(
                checked = foreGroundPermissionChecked,
                onCheckedChange = { isChecked ->
                    foreGroundPermissionChecked = isChecked
                    if (isChecked){
                        foreGroundPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ){
            Text(text = "Background Permissions")
            Checkbox(
                checked = backGroundPermissionChecked,
                onCheckedChange = { isChecked ->
                    backGroundPermissionChecked = isChecked
                    if (isChecked){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            backGroundPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        }
                    }
                }
            )
        }
        Button(onClick = {
            if(foreGroundPermissionChecked && backGroundPermissionChecked)
                navController.navigate("debugScreen")
        }) {
            Text(text = "continua")

        }
    }
}