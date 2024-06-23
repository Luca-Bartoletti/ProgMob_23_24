package com.example.betterpath.viewModel

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.betterpath.data.PathData
import com.example.betterpath.database.MyAppDatabase
import com.example.betterpath.foreground.ForegroundLocation
import com.example.betterpath.repository.LocationRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale


class LocationViewModel(
    private val context: Context,
    database: MyAppDatabase,
    private val historyViewModel: HistoryViewModel
) : ViewModel() {


    /**
     * variabile per la raccolta dei dati dal sensore di LOCAZIONE
     * */
    private val _locationData = MutableStateFlow<List<PathData?>>(emptyList())
    /**
     * espone in maniera non modificabile `_locationData`
     * @see _locationData
     * */
    val locationData = _locationData.asStateFlow()

    /**
     * Tiene traccia dell ultimo valore ricevuto in modo da non salvare dati superflui
     * per il tracciamento degli spostamenti
     * */
    private var _lastLocationData = MutableStateFlow<LatLng?>(null)

    /**
     * Verifica lo stato del permesso dell'utente per la localizzazione in ForeGround
     * */
    var hasForeGroundPermission = MutableStateFlow(getForeGroundPermissionStatus())
        private set
    /**
     * Verifica lo stato del permesso dell'utente per la localizzazione in BackGround
     * */
    var hasBackGroundPermission = MutableStateFlow(getBackGroundPermissionStatus())
        private set
    /**
     * Verifica lo stato del permesso dell'utente per l'invio di notifiche
     * */
    var hasNotificationPermission = MutableStateFlow(getNotificationPermissionStatus())
        private set

    /**
     * traccia lo stato di tracciamennto: `attivo -> true`, `spento -> false`
     */
    var isTracking = MutableStateFlow(false)

    /**
     * Identificativo del percorso odierno, necessario per le operazioni sui dati raccolti
     * quali la rappresentazione a schermo ed il loro salvataggio
     */
    private var todayPathId: MutableStateFlow<Int> = historyViewModel.todayId

    // riferimenti a repository e Dao per le operazioni sui dati
    private var pathDataDao = database.pathDataDao()
    private val locationRepository: LocationRepository = LocationRepository(
        context = context, locationViewModel = this, dao = pathDataDao!!
    )

    /**
     * contiene la lista di `PathData` (`List<PathData?>`) ricevuta dal database in seguito ad una richiesta di fetch
     * @see PathData
     */
    var fetchedLocationData = locationRepository.fetchedData
        private set

    /**
     * supporta il `fetchedLocationData` in caso sia necessario confrontare più percorsi in parallelo
     * @see fetchedLocationData
     * */
    var fetchedLocationData2 = locationRepository.fetchedData2
        private set

    /**
     * indica se i dati in `fetchedLocationData` sono validi o meno
     * @see fetchedLocationData
     * */
    val locationDataReady = locationRepository.locationDataReady

    /**
     * indica se i dati in `fetchedLocationData2` sono validi o meno
     * @see `fetchedLocationData2`
     * */
    val location2DataReady = locationRepository.location2DataReady

    /**
     * contiene la differenza calcolata tra due percorsi nel metodo `comparePaths`
     * @see comparePaths
     * */
    var pathDifference = MutableStateFlow(0f)
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

    /**
     * indica se il calcolo delle coordinate di centro della mappa sia finito o meno
     * */
    private val _centerReady = MutableStateFlow(false)
    /**
     * espone in maniera non modificabile `_centerReady`
     * @see _centerReady
     * */
    var centerReady = _centerReady.asStateFlow()


    init {
        // dalla creazione di LocationViewModel fino alla sua distruzione vengono costantemente
        // letti i valori relativi alle posizioni ottenute dell'utente
        viewModelScope.launch {
            locationRepository.locationFlow.collect { newLocation ->

                // se non ho ancora ottenuto delle locazioni salvo la prima ottenuta in maniera indiscriminata
                if (_lastLocationData.value == null) {
                    _lastLocationData.value =
                        LatLng(newLocation.latitude, newLocation.longitude)
                    _locationData.value += PathData(
                        lat = newLocation.latitude,
                        lng = newLocation.longitude,
                        time = newLocation.time,
                        pathHistoryId = todayPathId.value,
                        // 1 -> start
                        // 2 -> onGoning
                        // 3 -> stop
                        startStop = 1
                    )
                } else {
                    val results = FloatArray(1)
                    // Calcolo la distanza tra il punto precedente e quello attuale.
                    // Non salvo solo se non mi sono mosso abbastanza.
                    // In questo modo non salvo posizioni superflue
                    Location.distanceBetween(
                        _lastLocationData.value!!.latitude, _lastLocationData.value!!.longitude,
                        newLocation.latitude, newLocation.longitude,
                        results
                    )
                    if (results[0] >= 7) {
                        _locationData.value += PathData(
                            lat = newLocation.latitude,
                            lng = newLocation.longitude,
                            time = newLocation.time,
                            pathHistoryId = todayPathId.value,
                            // 1 -> start
                            // 2 -> onGoning
                            // 3 -> stop
                            startStop = 2
                        )
                        _lastLocationData.value = LatLng(newLocation.latitude, newLocation.longitude)
                    }
                }
            }
        }

        viewModelScope.launch {
            while (true) {
                // ripeto ogni 30 minuti
                delay(30 * 60 * 1000L)
                if (isTracking.value) {
                    // fermo temporaneamente la regisrazione dei dati, salvo  e riprendo
                    stopLocationUpdates()
                    startLocationUpdates(context)
                }
            }
        }
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


    /**
     * avvio il tracciamente dell'utente da parte dell'applicazione
     * e modifica il Flow `isTracking`
     * */
    fun startLocationUpdates(context: Context) {
        Intent(context, ForegroundLocation::class.java).also {
            it.action = ForegroundLocation.Actions.START.toString()
            context.startService(it)
        }
        locationRepository.startLocationUpdates()
        isTracking.value = true
        println("start tracking")

    }

    /**
     * interrompe il tacciamento dell'utente, modifica il flow `isTracking`,
     * salva i dati raccolti sul db e aggiorna lo stato di `fetchedLocationData`
     * @see saveDataAndClear
     * @see fetchedLocationData
     * */
    fun stopLocationUpdates(context : Context? = null) {
        if (context != null) {
            // elimino la notifica
            Intent(context, ForegroundLocation::class.java).also {
                it.action = ForegroundLocation.Actions.STOP.toString()
                context.startService(it)
            }
        }
        saveDataAndClear()
        locationRepository.getPathDataFromPathHistoryId(todayPathId.value)
        locationRepository.stopLocationUpdates()
        isTracking.value = false
        println("stop Tracking")
    }

    /**
     * salva le posizioni osservate, aggiorna i valori delle distanze percorse e
     * imposta i dati registrati alla lista vuota (`_locationData.value = emptyList()`)
     * @see com.example.betterpath.viewModel.HistoryViewModel.updateDistance
     * */
    fun saveDataAndClear() {
        if (_locationData.value.isNotEmpty()) {
            locationRepository.saveData(_locationData.value)
            historyViewModel.updateDistance(_locationData.value)
            _locationData.value = emptyList()
        }
    }

    fun getTodayPathData(){
        viewModelScope.launch {
            locationRepository.getPathDataFromPathHistoryId(todayPathId.value)
        }
    }

    fun getLocationData1And2(){
        locationRepository.getLocation1And2(historyViewModel.checkedBox.value[0], historyViewModel.checkedBox.value[1])
    }

    /**
     * aggiorno i valori di `maxLat`, `minLat`, `maxLng` e `minLng`
     * in accordo ai valori della lista passata
     * @param locationVals lista di `PathData` su cui calcolare i valori di massimo e minimo
     * */
    fun getMaxMinLatLng(locationVals : List<PathData?>){
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

    /**
     * richiama `getClusteredList` su due liste di PathData (`fetchedLocationData` e
     * `fetchedLocationData2`) per ottenere una stima dei percorsi;
     * imposta il valore di `pathDifference` in accordo ai valori calcolati dalla funzione
     * `countDifferentPoints`
     * @see pathDifference
     * @see countDifferentPoints
     * @see getClusteredList
     * */
    fun comparePaths(){
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                pathDifference.value = 0f
                val list1 = getClusteredList(fetchedLocationData.value)
                val list2 = getClusteredList(fetchedLocationData2.value)
                pathDifference.value = countDifferentPoints(list1, list2)
            }
        }
    }

    /**
     * data una `List<PathData?>` calcola un sottoinsieme di punti raggruppado tutti punti tali che
     * l'arrotondamento dei valori di latitudine e longitudine sia lo stesso.
     * Per esempio i punti
     * (49.072173900444895, 10.664321781015415) e (49.075173900444895, 10.664321781015415)
     * sono considerati separati ma
     * (49.072673900444895, 10.664333584255770) e (49.072813355871578, 10.664321781015415)
     * sono considerati appartenenti allo stesso cluster
     * */
    fun getClusteredList(list : List<PathData?>) : Set<LatLng>{
        val clusteredList = mutableSetOf<LatLng>()
        val numericFormat = NumberFormat.getInstance()
        var roundedLat: Double
        var roundedLng :Double
        val locale = Locale.ITALY
        for (data in list){
            data?.let {
                val latStr = String.format(locale,"%.3f", data.lat)
                val lngStr = String.format(locale,"%.3f", data.lng)
                roundedLat = numericFormat.parse(latStr)?.toDouble() ?: 0.0
                roundedLng = numericFormat.parse(lngStr)?.toDouble() ?: 0.0
                clusteredList.add(LatLng(roundedLat, roundedLng))
            }
        }
        return clusteredList
    }

    /**
     * calcola la differenza tra due insieme di `LatLng` verificando il numero di punti non presenti
     * nella loro intersezione
     * */
    fun countDifferentPoints(set1: Set<LatLng>, set2: Set<LatLng>): Float {
        val allPoints = set1.union(set2)
        val commonPoints = set1.intersect(set2)
        return if (allPoints.isEmpty()) 1f else (allPoints.size.toFloat() - commonPoints.size.toFloat())/allPoints.size.toFloat()
    }

    fun isGpsEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

}