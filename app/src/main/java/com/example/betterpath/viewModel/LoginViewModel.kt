package com.example.betterpath.viewModel

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.betterpath.firebase_signIn.GoogleAuthUiClient
import com.example.betterpath.firebase_signIn.SignInResult
import com.example.betterpath.firebase_signIn.SignInState
import com.example.betterpath.repository.PreferenceRepository
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class LoginViewModel(private val repository: PreferenceRepository, val googleAuthUiClient:GoogleAuthUiClient) : ViewModel() {
    val isLogged : Flow<Boolean> = repository.isUserLoggedInFlow
    var isMenuOpen = mutableStateOf(false)
        private set
    val isFirstTime : Flow<Boolean> = repository.isUserFirstTimeFlow

    private val _googleLoginState = MutableStateFlow(SignInState())
    val googleLoginState = _googleLoginState.asStateFlow()



    class LoginViewModelFactory(private val repository : PreferenceRepository, private val googleAuthUiClient:GoogleAuthUiClient): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(repository, googleAuthUiClient) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    fun saveUserFirstTime(isFirstTime: Boolean) = viewModelScope.launch {
        repository.setUserFirstTime(isFirstTime)
    }

    fun login() = viewModelScope.launch {
        repository.setUserLoggedIn(true)
    }

    fun logout() {
        isMenuOpen.value = false
        viewModelScope.launch {
            repository.setUserLoggedIn(false)
        }
        resetGoogleState()
    }

    fun openMenu() {
        isMenuOpen.value = true
    }

    fun closeMenu() {
        isMenuOpen.value = false
    }

    fun onSignInResult(result: SignInResult){
        _googleLoginState.update { it.copy(
            isSignInSuccessful =  result.data !=null,
            signInError = result.errorMessage
        ) }
    }

    fun resetGoogleState(){
        _googleLoginState.update{ SignInState() }
    }




}