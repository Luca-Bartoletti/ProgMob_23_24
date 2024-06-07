package com.example.betterpath.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun LoginForm(){
    var text by remember{ mutableStateOf("") }
    var password by rememberSaveable {
        mutableStateOf("")
    }

    Column {
        OutlinedTextField(
            value = text,
            onValueChange = {text = it},
            label = { Text(text = "Enter username")}

        )

        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = { Text("Enter password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

    }

}