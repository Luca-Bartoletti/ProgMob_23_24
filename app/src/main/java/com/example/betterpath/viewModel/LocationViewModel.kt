package com.example.betterpath.viewModel

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.Manifest
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.betterpath.database.MyAppDatabase
import com.example.betterpath.repository.HistoryRepository
import com.example.betterpath.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(
    private val context: Context,
    database: MyAppDatabase,
    historyViewModel: HistoryViewModel
) : ViewModel() {

    private val _locationData = MutableStateFlow<List<Location?>>(emptyList())
    val locationData = _locationData.asStateFlow()
    var hasForeGroundPermission = MutableStateFlow(getForeGroundPermissionStatus())
        private set
    var hasBackGroundPermission = MutableStateFlow(getBackGroundPermissionStatus())
        private set
    var isTracking = MutableStateFlow(false)
    private var pathId: Int = historyViewModel.todayId.value

    private var pathDataDao = database.pathDataDao()
    private val locationRepository: LocationRepository = LocationRepository(
        context = context, locationViewModel = this, dao = pathDataDao!!
    )
    var fetchedLocationData = locationRepository.fetchedData


    init {
        viewModelScope.launch {
            locationRepository.locationFlow.collect { newLocation ->
                _locationData.value += newLocation
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

    override fun onCleared() {
        super.onCleared()
        locationRepository.stopLocationUpdates()
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

    fun startLocationUpdates() {
        locationRepository.startLocationUpdates()
        isTracking.value = true
        println("start tracking")
    }

    fun stopLocationUpdates() {
        saveDataAndClear()
        locationRepository.stopLocationUpdates()
        isTracking.value = false
        println("stop Tracking")
    }

    fun saveDataAndClear() {
        if (_locationData.value.isNotEmpty()) {
            locationRepository.saveData(_locationData.value, pathId)
            _locationData.value = emptyList()
        }
    }

}