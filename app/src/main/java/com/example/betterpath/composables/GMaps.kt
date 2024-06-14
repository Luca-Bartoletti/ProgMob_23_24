package com.example.betterpath.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun GMaps(lat: Double = 45.09001835537128, lng: Double = 7.659142009974476) {
    val university = LatLng(lat, lng)
    val universityState = MarkerState(position = university)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(university, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(compassEnabled = false, zoomControlsEnabled = false)
    ){
        Marker(
            state = universityState,
            title = "DiUnito",
            snippet = "Marker in DiUnito"
        )
    }
}