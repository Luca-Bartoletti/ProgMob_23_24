package com.example.betterpath.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class PreferenceRepository (private val context: Context) {

    val isUserLoggedInFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_USER_LOGGED_IN] ?: false
    }
    val isUserFirstTimeFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_USER_FIRST_TIME] ?: true
    }

    suspend fun setUserLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_USER_LOGGED_IN] = isLoggedIn
        }
    }

    suspend fun setUserFirstTime(isFirstTime: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_USER_FIRST_TIME] = isFirstTime
        }
    }

    private object PreferencesKeys {
        val IS_USER_LOGGED_IN = booleanPreferencesKey("is_user_logged_in")
        val IS_USER_FIRST_TIME = booleanPreferencesKey("is_user_first_time")
    }


}