package com.example.betterpath.composables

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.betterpath.R
import com.example.betterpath.ui.theme.BetterPathTheme

@Composable
fun LoginScreen(navController: NavController){
    val context = LocalContext.current
    Column (
        Modifier.background(MaterialTheme.colorScheme.background)
    ){
        Header(context = context)
        Column (
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ){
            Row {
                LoginForm()
            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                              // TODO implementare il controllo di username e password
                              navController.navigate("mainRoute")
                              },
                    modifier = Modifier
                        .padding(end = 32.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)

                ) {
                    Text(text = context.getString(R.string.login_button))
                }
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
        AppNavigation(PaddingValues(0.dp))
}