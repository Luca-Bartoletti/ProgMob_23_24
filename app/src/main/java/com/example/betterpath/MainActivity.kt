package com.example.betterpath

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.betterpath.composables.AppNavigation
import com.example.betterpath.database.MyAppDatabase
import com.example.betterpath.repository.LocationRepository
import com.example.betterpath.repository.PreferenceRepository
import com.example.betterpath.ui.theme.BetterPathTheme
import com.example.betterpath.viewModel.HistoryViewModel
import com.example.betterpath.viewModel.LocationViewModel
import com.example.betterpath.viewModel.LoginViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.FOREGROUND_SERVICE_LOCATION),
                    0
                )
            }

        setContent {
            val database = MyAppDatabase.getDatabase(this)
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            BetterPathTheme(darkTheme = false) {

                val historyViewModel : HistoryViewModel by viewModels{
                    object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
                                @Suppress("UNCHECKED_CAST")
                                return HistoryViewModel(database!!) as T
                            }
                            throw IllegalArgumentException("Unknown ViewModel class")
                        }
                    }
                }

                val loginViewModel : LoginViewModel by viewModels{
                    LoginViewModel.LoginViewModelFactory(PreferenceRepository(applicationContext))
                }

                val context = LocalContext.current
                val locationViewModel: LocationViewModel by viewModels {
                    object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
                                @Suppress("UNCHECKED_CAST")
                                return LocationViewModel(context, database!!, historyViewModel) as T
                            }
                            throw IllegalArgumentException("Unknown ViewModel class")
                        }
                    }
                }

                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        println("EVENTO :: $event -- #eventi :: ${locationViewModel.locationData.value.size}")
                        if (event == Lifecycle.Event.ON_RESUME) {
                            if (locationViewModel.isTracking.value) locationViewModel.startLocationUpdates()
                        } else if (event == Lifecycle.Event.ON_DESTROY){
                            locationViewModel.saveDataAndClear()
                            locationViewModel.stopLocationUpdates()
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

                AppNavigation(historyViewModel, loginViewModel, locationViewModel)

            }
        }
    }

}