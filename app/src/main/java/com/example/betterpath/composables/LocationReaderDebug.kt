package com.example.betterpath.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.betterpath.viewModel.LocationViewModel


@Composable
fun LocationReaderDebug(locationViewModel: LocationViewModel) {
    MyCheck(locationViewModel = locationViewModel)
}

@Composable
fun MyCheck(locationViewModel: LocationViewModel){
    val location = locationViewModel.locationData.collectAsState().value
    val oldLocation = locationViewModel.fetchedLocationData.collectAsState().value
    locationViewModel.getMaxMinLatLon(oldLocation)

    Column(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxWidth()
    ) {
        Text(text = "valori salvati in precedenza")
        Column(
            modifier = Modifier
                .selectableGroup()
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp)
                .weight(0.2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "min Lat = ${locationViewModel.minLat}")
            Text(text = "max Lat = ${locationViewModel.maxLat}")
            Text(text = "min Lng = ${locationViewModel.minLng}")
            Text(text = "max Lat = ${locationViewModel.maxLng}")

        }
        LazyColumn(
            modifier = Modifier
                .selectableGroup()
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp)
                .weight(0.2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            locationViewModel.getTodayPathData()
            items(oldLocation.size) { index ->
                val path = oldLocation[index]
                path?.let { LogRow(lat = path.lat, lng = path.lng) } ?: BasicTextField(
                    value = "nessun dato disponibile al momento",
                    onValueChange = {},
                    readOnly = true
                )

            }
        }

        Text(text = "Numero di dati registrtati ${location.size}")

        LazyColumn(
            modifier = Modifier
                .selectableGroup()
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .weight(0.4f),
        ) {

            items(location.size) { index ->
                val path = location[index]
                path?.let { LogRow(lat = path.latitude, lng = path.longitude) } ?: BasicTextField(
                    value = "nessun dato disponibile al momento",
                    onValueChange = {},
                    readOnly = true
                )

            }

        }
    }
}

@Composable
fun LogRow(lat: Double?, lng: Double?){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ){
        Text(
            text = "lat : $lat - lng : $lng",
        )
    }
}
