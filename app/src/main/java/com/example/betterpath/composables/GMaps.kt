package com.example.betterpath.composables

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.betterpath.data.PathData
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun GMaps(centerLat: Double? = null, centerLng: Double? = null,
          points : List<PathData?>, points2 : List<PathData?> = emptyList(), numberOfPath: Int = 1) {
    val center =
        if (centerLat == null || centerLng == null)
            LatLng(45.09001835537128, 7.659142009974476)
        else LatLng(centerLat, centerLng)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(center, 15f)
    }
    LocalContext.current

    val pointList1 = mutableListOf<MutableList<LatLng>>()
    val pointList2 = mutableListOf<MutableList<LatLng>>()
    var index1 = 0
    try {
        for(point in points){
            when(point?.startStop){
                1 -> pointList1 += mutableListOf(LatLng(point.lat,point.lng))
                2 -> pointList1[index1] += LatLng(point.lat,point.lng)
                3 -> pointList1[index1++] += LatLng(point.lat,point.lng)
            }
        }
    } catch (e : Error){
        println("catturato errore: $e")
        println("sui dati: $points")
    }

    if (numberOfPath == 2){
        var index2 = 0
        for(point in points2){
            when(point?.startStop){
                1 -> pointList2 += mutableListOf(LatLng(point.lat, point.lng))
                2 -> pointList2[index2] += LatLng(point.lat, point.lng)
                3 -> pointList2[index2++] += LatLng(point.lat, point.lng)
                4 -> {
                    pointList2 += mutableListOf(LatLng(point.lat,point.lng))
                    index2++
                }
            }
        }
    }

    isSystemInDarkTheme()
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(compassEnabled = false, zoomControlsEnabled = false),
    ) {
        for (list in pointList1) {
            MyPolyline(
                pointList = list,
                color = MaterialTheme.colorScheme.primary
            )
        }
        if (numberOfPath == 2) {
            for (list in pointList2) {
                println("points2.size = ${points2.size}")
                MyPolyline(
                    pointList = list,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            println("colore percorso 2 : ${MaterialTheme.colorScheme.tertiary.value}")
        }
    }

}

@Composable
fun MyPolyline(pointList: MutableList<LatLng>, color : Color){
    Polyline(
        points = pointList,
        clickable = false,
        visible = true,
        color = color,
        startCap = RoundCap(),
        endCap = RoundCap()
    )
}