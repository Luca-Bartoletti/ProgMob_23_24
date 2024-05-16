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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.betterpath.R

@Composable
fun Header(context: Context){
    val brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.60f to MaterialTheme.colorScheme.secondary,
            1f to MaterialTheme.colorScheme.background
        )
    )
    Row (
        modifier = Modifier
            .background(brush)
            .fillMaxWidth()
            .height(96.dp),
    ){
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                Text(
                    text = context.getString(R.string.app_name),
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding( bottom =8.dp )
                )
            }
        }
        Spacer(modifier = Modifier.weight(4f))
        Row (
            horizontalArrangement = Arrangement.End
        ){
            Image(
                painter = painterResource(id = R.drawable.logo_ia),
                contentDescription = context.getString(R.string.application_logo),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp, bottom = 8.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    Header(context = LocalContext.current)
}