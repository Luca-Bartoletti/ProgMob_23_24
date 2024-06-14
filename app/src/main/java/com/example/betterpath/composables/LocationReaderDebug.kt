package com.example.betterpath.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.betterpath.viewModel.LocationViewModel

@Composable
fun LocationReaderDebug(locationViewModel: LocationViewModel){
   val location = locationViewModel.locationData.collectAsState().value

    Column(
        modifier = Modifier
            .selectableGroup()
            .fillMaxWidth()
            .systemBarsPadding()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogRow(lat = location?.latitude, lng = location?.longitude )
    }
}

@Composable
fun LogRow(lat: Double?, lng: Double?){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ){
        Text(text = "lat : $lat - lng : $lng")
    }
}
