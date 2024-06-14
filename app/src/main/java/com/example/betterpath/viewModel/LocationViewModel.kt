package com.example.betterpath.viewModel

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.Manifest
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.betterpath.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel (private val context: Context, private val locationRepository: LocationRepository) : ViewModel(){
    private val _locationData = MutableStateFlow<Location?>(null)
    val locationData = _locationData.asStateFlow()
    var hasForeGroundPermission = MutableStateFlow(getForeGroundPermissionStatus())
        private set
    var hasBackGroundPermission = MutableStateFlow(getBackGroundPermissionStatus())
        private set

    init {
        viewModelScope.launch {
            locationRepository.locationFlow.collect{
                _locationData.value = it
            }
        }
    }

    private fun getForeGroundPermissionStatus(): Boolean{
        return if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            true
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun getBackGroundPermissionStatus():Boolean{
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            true
        } else{
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

    fun updateBackGroundPermissionStatus(hasPermission: Boolean){
        viewModelScope.launch {
            hasBackGroundPermission.value = hasPermission
        }
    }
    fun updateForeGroundPermissionStatus(hasPermission: Boolean){
        viewModelScope.launch {
            hasForeGroundPermission.value = hasPermission
        }
    }

    fun startLocationUpdates(){
        locationRepository.startLocationUpdates()
    }

    fun stopLocationUpdates(){
        locationRepository.stopLocationUpdates()
    }

}