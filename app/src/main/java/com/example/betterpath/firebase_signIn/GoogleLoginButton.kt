package com.example.betterpath.firebase_signIn

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.betterpath.R
import com.example.betterpath.viewModel.LoginViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

@Composable
fun GoogleLoginButton(
    loginViewModel: LoginViewModel,
    navController: NavController,
    modifier: Modifier,
    isMenu : Boolean = false,
    isLogIn: Boolean = true
){

    val context = LocalContext.current.applicationContext
    val coroutineScope = rememberCoroutineScope()
    val loginLoginState = loginViewModel.googleLoginState.collectAsState()

    val googleAuthUiClient = loginViewModel.googleAuthUiClient

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                loginViewModel.viewModelScope.launch {
                    val signInResult = googleAuthUiClient.getSignInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    loginViewModel.onSignInResult(signInResult)
                }
            }
        }
    )
    LaunchedEffect(loginLoginState.value.isSignInSuccessful) {
        if (loginLoginState.value.isSignInSuccessful) {
            if(isMenu){
                loginViewModel.login()
            }
            else {
                navController.navigate("mainRoute") {
                    loginViewModel.login()
                    popUpTo("loginRoute") {
                        inclusive = true
                    }
                }
            }
        }
    }


    if (isLogIn) {
        Button(
            onClick = {
                coroutineScope.launch {
                    val signInIntentSender = googleAuthUiClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentSender ?: return@launch
                        ).build()
                    )
                }
            },
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                color = MaterialTheme.colorScheme.onPrimary,
                text = context.getString(R.string.login_with_google)
            )
        }
    }
    else{
        Button(
            onClick = {
                coroutineScope.launch {
                    googleAuthUiClient.signOut()
                    Toast.makeText(
                        context,
                        "Logged out",
                        Toast.LENGTH_LONG
                    ).show()
                }
                navController.navigate("loginRoute"){
                    popUpTo("loginRoute"){
                        inclusive = true
                    }
                }
                loginViewModel.closeMenu()
                loginViewModel.logout()
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ){
            Text(
                color = MaterialTheme.colorScheme.onPrimary,
                text = stringResource(R.string.logout)
            )
        }
    }
}