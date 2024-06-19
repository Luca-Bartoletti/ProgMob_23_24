package com.example.betterpath.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.betterpath.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class LoginViewModel(private val repository: PreferenceRepository) : ViewModel() {
    val isLogged : Flow<Boolean> = repository.isUserLoggedInFlow
    var isMenuOpen = mutableStateOf(false)
        private set
    val isFirstTime : Flow<Boolean> = repository.isUserFirstTimeFlow

    class LoginViewModelFactory(private val repository : PreferenceRepository): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(repository) as T
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
    }

    fun openMenu() {
        isMenuOpen.value = true
    }

    fun closeMenu() {
        isMenuOpen.value = false
    }


}