package com.example.betterpath.composables

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.betterpath.R
import com.example.betterpath.viewModel.LoginViewModel

@Composable
fun Header(context: Context = LocalContext.current,
           navController: NavController, loginViewModel: LoginViewModel){
    val brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.60f to MaterialTheme.colorScheme.tertiary, //0.60f
            1f to MaterialTheme.colorScheme.background
        )
    )
    val backstackEntry = navController.currentBackStackEntry?.destination
    Row (
        modifier = Modifier
            .background(brush)
            .fillMaxWidth()
            .systemBarsPadding()
            .height(96.dp)
            .padding(start = 16.dp),
    ){
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxHeight()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxHeight()
            ) {
                if (backstackEntry?.route != "loginScreen") {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Account image",
                        tint = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .scale(2f)
                            .clickable {
                                loginViewModel.openMenu()
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(4f))
            Row(
                horizontalArrangement = Arrangement.End
            ) {
                CircleImage(
                    resource = R.drawable.logo_ia,
                    contentDescription = context.getString(R.string.application_logo),
                    onClickAction = {
                        // TODO cambiare con home page a test finiti
                        navController.navigate("compareScreen"){
//                            popUpTo("home"){
//                                inclusive = true
//                            }
                        }
                    }
                )
            }
        }
    }
}
@Composable
fun CircleImage(resource: Int, contentDescription: String?, onClickAction: () -> Unit){
    Image(
        painter = painterResource(id = resource),
        contentDescription = contentDescription,
        contentScale = ContentScale.FillHeight,
        modifier = Modifier
            .padding(top = 8.dp, end = 8.dp, bottom = 8.dp)
            .clip(CircleShape)
            .clickable(onClick = onClickAction)
    )
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    Header(navController = NavController(context = LocalContext.current), loginViewModel = LoginViewModel())
}