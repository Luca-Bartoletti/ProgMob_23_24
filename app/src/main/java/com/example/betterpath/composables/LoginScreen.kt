package com.example.betterpath.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.betterpath.R

@Composable
fun LoginScreen(navController: NavController){
    val context = LocalContext.current
    Column (
        Modifier.background(MaterialTheme.colorScheme.background)
    ){
        Header(context = context, navController = navController)
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 64.dp)
        ){

            Button(
                onClick = {
                    // TODO implementare API Google
                    navController.navigate("mainRoute") {
                        popUpTo("loginRoute") {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onPrimary,
                    text = context.getString(R.string.login_with_google)
                )
            }


            Spacer(modifier = Modifier.height(16.dp))


            Text(
                color = MaterialTheme.colorScheme.onBackground,
                text = context.getString(R.string.or_string),
            )


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // TODO implementare il controllo di username e password
                    navController.navigate("mainRoute") {
                        popUpTo("loginRoute") {
                            inclusive = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth()

            ) {
                Text(
                    color = MaterialTheme.colorScheme.onPrimary,
                    text = context.getString(R.string.login_offline_button)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                color = MaterialTheme.colorScheme.onBackground,
                text = stringResource(R.string.offline_note),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Justify
            )

        }
    }

}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = NavController(LocalContext.current))
}