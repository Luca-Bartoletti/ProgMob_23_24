package com.example.betterpath.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.betterpath.data.PathData
import com.example.betterpath.viewModel.LocationViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PatternItem
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun GMaps(centerLat: Double = 45.09001835537128, centerLng: Double = 7.659142009974476,
          points : List<PathData?>, points2 : List<PathData?> = emptyList(), numberOfPath: Int = 1) {
    val center = LatLng(centerLat, centerLng)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(center, 15f)
    }

    val pointList1 = mutableListOf<LatLng>()
    val pointList2 = mutableListOf<LatLng>()
    points.map { point ->
        pointList1 += LatLng(point!!.lat,point.lng)
    }
    if (numberOfPath == 2){
        points2.map { point ->
            pointList2 += LatLng(point!!.lat,point.lng)
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(compassEnabled = false, zoomControlsEnabled = false)
    ){

        Polyline(
            points = pointList1,
            clickable = false,
            visible = true,
            color = MaterialTheme.colorScheme.secondary,
        )

        if (numberOfPath == 2){
            Polyline(
                points = pointList2,
                clickable = false,
                visible = true,
                color = MaterialTheme.colorScheme.surface,
            )
        }
    }
}