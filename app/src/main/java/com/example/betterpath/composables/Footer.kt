package com.example.betterpath.composables

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.betterpath.R
import com.example.betterpath.viewModel.HistoryViewModel
import com.example.betterpath.viewModel.LocationViewModel

@Composable
fun Footer(navController: NavController? = null, context: Context = LocalContext.current, historyViewModel: HistoryViewModel, locationViewModel: LocationViewModel,
           homeButton : Boolean = false, historyButton : Boolean = false, compareButton: Boolean = false){
    val brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to MaterialTheme.colorScheme.background,
            0.00f to MaterialTheme.colorScheme.tertiary, //0.20f
        )
    )

    val isCompareButtonEnabled = historyViewModel.enableCompareButton.value
    Row (
        modifier = Modifier
            .background(brush)
            .fillMaxWidth()
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column {
            if (homeButton) {
                Button(
                    onClick = {
                        navController?.navigate("mainScreen"){
                            popUpTo("mainScreen"){
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier.padding(start = 32.dp, bottom = 32.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)

                ) {
                    Text(
                        text = context.getString(R.string.home),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        Column {
            if (historyButton) {
                Button(
                    onClick = {
                        navController?.navigate("historyScreen")
                    },
                    modifier = Modifier.padding(end = 32.dp, bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = context.getString(R.string.history),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            } else if(compareButton){
                Button(
                    onClick = {
                        locationViewModel.getLocationData1And2()
                        historyViewModel.fetchFirstPath()
                        historyViewModel.fetchSecondPath()
                        navController?.navigate("compareScreen")
                    },
                    enabled = isCompareButtonEnabled,
                    modifier = Modifier.padding(end = 32.dp, bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = stringResource(R.string.compare),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

    }
}

//@Preview(showBackground = true)
//@Composable
//fun FooterPreview() {
//    Footer(homeButton = true, historyButton = true, historyViewModel = HistoryViewModel())
//}