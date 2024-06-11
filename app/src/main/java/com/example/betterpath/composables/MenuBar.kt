package com.example.betterpath.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.betterpath.R
import com.example.betterpath.viewModel.LoginViewModel

@Composable
fun MenuBar(viewModel: LoginViewModel, navController: NavController) {

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
                .padding(top = 16.dp)
        ) {
            if (viewModel.isLogged.value) {
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
                            viewModel.logout()
                            navController.navigate("loginRoute"){
                                popUpTo("loginRoute"){
                                    inclusive = true
                                }
                            }
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
                        viewModel.login()
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.onPrimary,
                        text = LocalContext.current.getString(R.string.login_with_google)
                    )
                }
            }
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close Menu",
                tint = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .scale(1f)
                    .clickable {
                        viewModel.closeMenu()
                    }
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
fun MenuBarPreview() {
    MenuBar(LoginViewModel(), navController = NavController(LocalContext.current) )
}