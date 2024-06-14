package com.example.betterpath

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
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
                                return LocationViewModel(context, LocationRepository(this@MainActivity)) as T
                            }
                            throw IllegalArgumentException("Unknown ViewModel class")
                        }
                    }
                }

                AppNavigation(historyViewModel, loginViewModel, locationViewModel)
            }
        }
    }
}