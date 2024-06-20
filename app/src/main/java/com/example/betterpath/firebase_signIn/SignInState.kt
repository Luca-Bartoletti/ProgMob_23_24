package com.example.betterpath.firebase_signIn

data class SignInState(
    val isSignInSuccessful : Boolean = false,
    val signInError : String? = null
)
