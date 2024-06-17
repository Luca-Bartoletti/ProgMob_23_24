package com.example.betterpath.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.betterpath.data.PathData
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun GMaps(centerLat: Double = 45.09001835537128, centerLng: Double = 7.659142009974476, markers : List<PathData?>) {
    val center = LatLng(centerLat, centerLng)
    println("$centerLat, $centerLat")
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(center, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(compassEnabled = false, zoomControlsEnabled = false)
    ){
        for (marker in markers){
            marker?.let {
                println("Marker${marker.id}: ${marker.lat},${marker.lng}")
                Marker(
                    state = MarkerState(LatLng(marker.lat,marker.lng)),
                    title = "marker_${marker.id}",
                    snippet = null
                )
            }
        }
    }
}