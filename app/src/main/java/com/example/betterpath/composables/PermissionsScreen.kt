package com.example.betterpath.composables

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.betterpath.R
import com.example.betterpath.viewModel.LocationViewModel
import com.example.betterpath.viewModel.LoginViewModel

@Composable
fun PermissionsScreen(locationViewModel: LocationViewModel, navController: NavController, loginViewModel: LoginViewModel){
    //val context = LocalContext.current
    var foreGroundPermissionChecked by remember { mutableStateOf(false) }
    var backGroundPermissionChecked by remember { mutableStateOf(false) }
    var notificationChecked by remember { mutableStateOf(false) }

    val foreGroundPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted : Boolean -> locationViewModel.updateForeGroundPermissionStatus(isGranted) }

    val backGroundPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted : Boolean -> locationViewModel.updateBackGroundPermissionStatus(isGranted) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted : Boolean -> locationViewModel.updateNotificationPermissionStatus(isGranted) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            //.systemBarsPadding()
            .background(MaterialTheme.colorScheme.background)

    ) {
        Header(navController = navController, loginViewModel = loginViewModel)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = stringResource(R.string.on_boarding),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Row (
                modifier = Modifier.fillMaxWidth()
            ){
                Text(text = stringResource(R.string.permissions_explanation),
                    textAlign = TextAlign.Justify,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
                        Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ){
                Text(
                    text = "Foreground Permissions",
                    color = MaterialTheme.colorScheme.onBackground
                )
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
                Text(
                    text = "Background Permissions",
                    color = MaterialTheme.colorScheme.onBackground
                )
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
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ){
                Text(
                    text = "Notification Permissions",
                    color = MaterialTheme.colorScheme.onBackground
                )
                Checkbox(
                    checked = notificationChecked,
                    onCheckedChange = { isChecked ->
                        notificationChecked = isChecked
                        if (isChecked){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            }
                        }
                    }
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                horizontalArrangement = Arrangement.End,
            ){
                Button(
                    enabled = foreGroundPermissionChecked && backGroundPermissionChecked && notificationChecked,
                    onClick = {
                        if (locationViewModel.hasForeGroundPermission.value && locationViewModel.hasBackGroundPermission.value && locationViewModel.hasNotificationPermission.value) {
                            loginViewModel.saveUserFirstTime(false)
                            navController.navigate("loginRoute"){
                                popUpTo("permissionScreen"){
                                    inclusive = true
                                }
                            }
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.continue_text))

                }
            }

        }

    }
}