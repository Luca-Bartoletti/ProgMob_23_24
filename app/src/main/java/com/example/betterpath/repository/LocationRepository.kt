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
    val locationDataReady = MutableStateFlow(false)

    private val _fetchedData2  = MutableStateFlow<List<PathData?>>(emptyList())
    val fetchedData2:StateFlow<List<PathData?>> = _fetchedData2.asStateFlow()
    val location2DataReady = MutableStateFlow(false)

    /**
     * avvia il necessario per la locazione in foreground e background tramite la creazione di una
     * notifica e indicando al `locationManager` di raccogliere dati ogni 10 secondi utilizzando il GPS
     * */
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

    fun saveData(values: List<PathData?>) {
        locationViewModel.viewModelScope.launch {
            val insertList = mutableListOf<PathData>()
            for (value in values) value?.let { insertList += value }
            if(insertList.size > 1)
                insertList[insertList.size-1].startStop = 3
            withContext(Dispatchers.IO) {
                dao.insertAll(insertList)
            }
        }
    }

    fun getPathDataFromPathHistoryId(id:Int){
        _fetchedData.value = emptyList()
        locationViewModel.viewModelScope.launch {
            withContext(Dispatchers.IO){
                _fetchedData.value = dao.getAllPathWithHistoryId(id)
            }
        }
    }

    fun getLocation1And2(id1 :Int, id2:Int){
        locationDataReady.value = false
        location2DataReady.value = false
        _fetchedData.value = emptyList()
        _fetchedData2.value = emptyList()
        locationViewModel.viewModelScope.launch {
            withContext(Dispatchers.IO){
                _fetchedData.value = dao.getAllPathWithHistoryId(id1)
            }
            locationDataReady.value = true
        }
        locationViewModel.viewModelScope.launch {
            withContext(Dispatchers.IO){
                _fetchedData2.value = dao.getAllPathWithHistoryId(id2)
            }
            location2DataReady.value = true
        }
    }
}