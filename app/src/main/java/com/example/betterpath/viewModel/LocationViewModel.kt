package com.example.betterpath.viewModel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.betterpath.data.PathData
import com.example.betterpath.database.MyAppDatabase
import com.example.betterpath.repository.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LocationViewModel(
    private val context: Context,
    database: MyAppDatabase,
    historyViewModel: HistoryViewModel
) : ViewModel() {


    // variabile per la raccolta dei dati dal sensore di LOCAZIONE
    private val _locationData = MutableStateFlow<List<PathData?>>(emptyList())
    val locationData = _locationData.asStateFlow()

    //variabile per segnare la prima posizione dell'utente nota
//    private val _firstLocation = MutableStateFlow<Location?>(null)
//    val firstLocation = _firstLocation

    // Variabili per memorizzare se siano presenti i permessi espliciti per l'uso della posizione
    //da parte dell'utente
    var hasForeGroundPermission = MutableStateFlow(getForeGroundPermissionStatus())
        private set
    var hasBackGroundPermission = MutableStateFlow(getBackGroundPermissionStatus())
        private set
    var hasNotificationPermission = MutableStateFlow(getNotificationPermissionStatus())
        private set

    //variabile per tenere traccia dello stato di tracciamennto: attivo o meno
    var isTracking = MutableStateFlow(false)

    // Identificativo del percorso odierno, necessario per le operazioni sui dati raccolti
    // quali la rappresentazione a schermo ed il loro salvataggio
    private var todayPathId: Int = historyViewModel.todayId.value

    // riferimenti a repository e Dao per le operazioni sui dati
    private var pathDataDao = database.pathDataDao()
    private val locationRepository: LocationRepository = LocationRepository(
        context = context, locationViewModel = this, dao = pathDataDao!!
    )

    // contenitore per i dati raccolti dal Repository.
    // Continene un lista di PathData?
    var fetchedLocationData = locationRepository.fetchedData
        private set

    // per settare la dimensione della mappa sullo span del percorso tengo in memoria i valori
    // massimi e minimi di latitudine e longitudine raccolti
    var maxLat: Double = Double.MIN_VALUE
        private set
    var maxLng: Double = Double.MIN_VALUE
        private set
    var minLat: Double = Double.MAX_VALUE
        private set
    var minLng: Double = Double.MAX_VALUE
        private set
    private val _centerReady = MutableStateFlow(false)
    var centerReady = _centerReady.asStateFlow()

    init {
        // dalla creazione di LocationViewModel fino alla sua distruzione vengono costantemente
        // letti i valori relativi alle posizioni ottenute dell'utente
        viewModelScope.launch {
            locationRepository.locationFlow.collect { newLocation ->
                _locationData.value += PathData(
                    lat = newLocation.latitude,
                    lng = newLocation.longitude,
                    time = newLocation.time,
                    pathHistoryId = todayPathId
                )
            }
        }
        locationRepository.fakeinsert()

    }

    private fun getForeGroundPermissionStatus(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            true
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun getBackGroundPermissionStatus(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            true
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun getNotificationPermissionStatus(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            true
        } else {
           ContextCompat.checkSelfPermission(
               context,
               Manifest.permission.POST_NOTIFICATIONS
           ) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }

    fun updateBackGroundPermissionStatus(hasPermission: Boolean) {
        viewModelScope.launch {
            hasBackGroundPermission.value = hasPermission
        }
    }

    fun updateForeGroundPermissionStatus(hasPermission: Boolean) {
        viewModelScope.launch {
            hasForeGroundPermission.value = hasPermission
        }
    }

    fun updateNotificationPermissionStatus(hasPermission: Boolean){
        viewModelScope.launch {
            hasNotificationPermission.value = hasPermission
        }
    }

    fun startLocationUpdates() {

        locationRepository.startLocationUpdates()

        isTracking.value = true
        println("start tracking")
    }

    fun stopLocationUpdates() {
        saveDataAndClear()
        locationRepository.getPathDataFromPathHistoryId(todayPathId)
        locationRepository.stopLocationUpdates()
        isTracking.value = false
        println("stop Tracking")
    }

    fun saveDataAndClear() {
        if (_locationData.value.isNotEmpty()) {
            locationRepository.saveData(_locationData.value)
            _locationData.value = emptyList()
        }
    }

    fun getTodayPathData(){
        viewModelScope.launch {
            locationRepository.getPathDataFromPathHistoryId(todayPathId)
        }
    }

    fun getMaxMinLatLon(locationVals : List<PathData?>){
        _centerReady.value = false
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                for (loc in locationVals){
                    loc?.let {
                        if(loc.lat > maxLat) maxLat = loc.lat
                        if(loc.lat < minLat) minLat = loc.lat
                        if(loc.lng > maxLng) maxLng = loc.lng
                        if(loc.lng < minLng) minLng = loc.lng
                    }
                }
            }
                _centerReady.value = true
        }
    }

}