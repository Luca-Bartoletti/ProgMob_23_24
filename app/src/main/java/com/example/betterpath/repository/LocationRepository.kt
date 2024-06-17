package com.example.betterpath.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewModelScope
import com.example.betterpath.data.PathData
import com.example.betterpath.data.PathDataDao
import com.example.betterpath.viewModel.LocationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationRepository(
    private val context: Context,
    private val dao: PathDataDao,
    private val locationViewModel: LocationViewModel,
) {

    private val locationManager: LocationManager
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

    private val _fetchedData  = MutableStateFlow<List<PathData?>>(emptyList())
    val fetchedData:StateFlow<List<PathData?>> = _fetchedData.asStateFlow()

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
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            10000, 0f,
            locationListener,
        )
    }

    fun stopLocationUpdates() {
        locationManager.removeUpdates(locationListener)
    }

    fun saveData(values: List<Location?>, historyId: Int) {
        locationViewModel.viewModelScope.launch {
            val insertList = values.map { value ->
                PathData(
                    lat = value!!.latitude,
                    lng = value.longitude,
                    time = value.time,
                    pathHistoryId = historyId
                )
            }
            withContext(Dispatchers.IO) {
                dao.insertAll(insertList)
            }
        }
    }

    fun getPathDataFromPathHistoryId(id:Int){
        locationViewModel.viewModelScope.launch {
            withContext(Dispatchers.IO){
                _fetchedData.value = dao.getAllPathWithHistoryId(id)
            }
        }
    }
}