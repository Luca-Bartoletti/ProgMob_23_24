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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.betterpath.firebase_signIn.GoogleLoginButton
import com.example.betterpath.viewModel.LoginViewModel

@Composable
fun MenuBar(loginViewModel: LoginViewModel, navController: NavController) {

    val isUserLogged by loginViewModel.isLogged.collectAsState(initial = false)
    val userData = loginViewModel.googleAuthUiClient.getSignedInUser()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.tertiary)
            .width(LocalConfiguration.current.screenWidthDp.dp / 100 * 55)
            .padding(start = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.End
        ) {
            //Icona per la chiusura del menu
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


        if (isUserLogged) {
            if (userData?.pictureUrl != null) {

                AsyncImage(
                    model = userData.pictureUrl,
                    contentDescription = "profile picture",
                    modifier = Modifier
                        .scale(2f)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(32.dp))
            }

            if (userData?.username != null) {
                Text(
                    text = userData.username,
                    color = MaterialTheme.colorScheme.onTertiary,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }


            GoogleLoginButton(
                loginViewModel = loginViewModel,
                navController = navController,
                modifier = Modifier,
                isMenu = true,
                isLogIn = false,
            )
        } else {
            GoogleLoginButton(
                loginViewModel = loginViewModel,
                navController = navController,
                modifier = Modifier.padding(start = 8.dp),
                isMenu = true
            )
        }

        // todo rimuovere
//        Button(
//            onClick = {
//                navController.navigate("debugRoute")
//                loginViewModel.closeMenu()
//            },
//            //modifier = Modifier.padding(start = 8.dp),
//        ) {
//            Text(text = "DEBUG")
//        }

    }

}

//@Preview(showBackground = true)
//@Composable
//fun MenuBarPreview() {
//    MenuBar(
//        LoginViewModel(PreferenceRepository(LocalContext.current),
//            googleAuthUiClient = GoogleAuthUiClient(context = LocalContext.current.applicationContext,
//            oneTapClient = Identity.getSignInClient(LocalContext.current.applicationContext))),
//        navController = NavController(LocalContext.current)
//    )
//}