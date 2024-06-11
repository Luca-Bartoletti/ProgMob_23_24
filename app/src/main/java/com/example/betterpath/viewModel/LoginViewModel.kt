package com.example.betterpath.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var isLogged = mutableStateOf(false)
        private set

    var isMenuOpen = mutableStateOf(false)
        private set

    init {
        isLogged.value = false
        isMenuOpen.value = false
    }

    fun login() {
        isLogged.value = true
    }

    fun logout() {
        isLogged.value = false
        isMenuOpen.value = false
    }

    fun openMenu() {
        isMenuOpen.value = true
    }

    fun closeMenu() {
        isMenuOpen.value = false
    }


}