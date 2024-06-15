package com.example.betterpath.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class LocationRepository(private val context: Context) {
    private val locationManager : LocationManager
        = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private  val locationChannel = Channel<Location>(Channel.BUFFERED)
    val locationFlow : Flow<Location> = locationChannel.receiveAsFlow()

    private val locationListener = object : LocationListener{
        override fun onLocationChanged(location: Location) {
            locationChannel.trySend(location)
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    fun startLocationUpdates(){
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0f, locationListener)
    }

    fun stopLocationUpdates() {
        locationManager.removeUpdates(locationListener)
        println("location repository : stop location")
    }
}