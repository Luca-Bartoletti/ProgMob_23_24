package com.example.betterpath

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.tooling.preview.Preview
import com.example.betterpath.composables.AppNavigation
import com.example.betterpath.ui.theme.BetterPathTheme
import com.example.betterpath.viewModel.HistoryViewModel
import com.example.betterpath.viewModel.LoginViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            BetterPathTheme(darkTheme = false) {
                val historyViewModel : HistoryViewModel by viewModels()
                val loginViewModel : LoginViewModel by viewModels()
                    AppNavigation(historyViewModel, loginViewModel)
            }
        }
    }
}