package com.example.betterpath.composables

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.betterpath.R

@Composable
fun Header(isLogged : Boolean = true, context: Context = LocalContext.current){
    val brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to MaterialTheme.colorScheme.tertiary, //0.60f
            1f to MaterialTheme.colorScheme.background
        )
    )
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
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 16.dp)
            ) {
                if (!isLogged) {
                    Text(
                        text = context.getString(R.string.app_name),
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                } else {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxHeight()

                    ){
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Account image",
                            tint = MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .scale(2f)
                        )
                        Column (
                            modifier = Modifier.padding(start = 8.dp)
                        ){
                            Text(
                                text = "prova",
                                color = MaterialTheme.colorScheme.onTertiary,
                            )
                            Text(
                                text = "placeholder_nome_utente",
                                color = MaterialTheme.colorScheme.onTertiary,)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(4f))
            Row(
                horizontalArrangement = Arrangement.End
            ) {
                CircleImage(
                    resource = R.drawable.logo_ia,
                    contentDescription = context.getString(R.string.application_logo)
                )
            }
        }
    }
}
@Composable
fun CircleImage(resource: Int, contentDescription: String?){
    Image(
        painter = painterResource(id = resource),
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .padding(top = 8.dp, end = 8.dp, bottom = 8.dp)
            .clip(CircleShape)
    )
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    Header(isLogged = true)
}