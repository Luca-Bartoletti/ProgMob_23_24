package com.example.betterpath.composables

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
        Header(context = context)
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ){
            Row {
                LoginForm()
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        //navController.navigate("mainRoute")
                    },
                    modifier = Modifier
                        .padding(start = 64.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ){
                    Text(text = context.getString(R.string.registration_button))
                }
                Button(
                    onClick = {
                              // TODO implementare il controllo di username e password
                              navController.navigate("mainRoute"){
                                  popUpTo("loginRoute"){
                                      inclusive = true
                                  }
                              }
                              },
                    modifier = Modifier
                        .padding(end = 64.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)

                ) {
                    Text(text = context.getString(R.string.login_button))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    color = MaterialTheme.colorScheme.onBackground,
                    text = context.getString(R.string.or_string)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Button(
                    onClick = {
                        // TODO implementare API Google
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ){
                    Text(text = context.getString(R.string.login_with_google))
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = NavController(LocalContext.current))
}