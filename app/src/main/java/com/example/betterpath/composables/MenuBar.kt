package com.example.betterpath.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.betterpath.R
import com.example.betterpath.repository.PreferenceRepository
import com.example.betterpath.viewModel.LoginViewModel

@Composable
fun MenuBar(loginViewModel: LoginViewModel, navController: NavController) {

    val isUserLogged by loginViewModel.isLogged.collectAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.tertiary)
            .width(LocalConfiguration.current.screenWidthDp.dp / 100 * 55)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close Menu",
                tint = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .scale(1f)
                    .clickable {
                        loginViewModel.closeMenu()
                    }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (isUserLogged) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Account image",
                    tint = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .scale(1.5f)
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "prova",
                        color = MaterialTheme.colorScheme.onTertiary,
                    )
                    Text(
                        text = "nome_utente",
                        color = MaterialTheme.colorScheme.onTertiary,
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            loginViewModel.logout()
                            navController.navigate("loginRoute"){
                                popUpTo("loginRoute"){
                                    inclusive = true
                                }
                            }
                            loginViewModel.closeMenu()
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                    ){
                        Text(
                            color = MaterialTheme.colorScheme.onPrimary,
                            text = stringResource(R.string.logout)
                        )
                    }
                }
            } else {
                Button(
                    modifier = Modifier.padding(start = 8.dp),
                    onClick = {
                        // TODO implementare API Google
                        loginViewModel.login()
                        loginViewModel.closeMenu()
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.onPrimary,
                        text = LocalContext.current.getString(R.string.login_with_google)
                    )
                }
            }
        }

        Button(
            onClick = {
                navController.navigate("debugRoute")
                loginViewModel.closeMenu()
            },
            modifier = Modifier.padding(start = 8.dp),
        ) {
            Text(text = "DEBUG")
        }

    }

}

@Preview(showBackground = true)
@Composable
fun MenuBarPreview() {
    MenuBar(
        LoginViewModel(PreferenceRepository(LocalContext.current)),
        navController = NavController(LocalContext.current) )
}