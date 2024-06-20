package com.example.betterpath.firebase_signIn

data class SignInResult(
    val data: UserData?,
    val errorMessage : String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val pictureUrl: String?
)
