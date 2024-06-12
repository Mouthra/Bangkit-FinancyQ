package com.example.financyq.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class UserPreferences(context: Context) {
    private val dataStore = context.dataStore

    suspend fun saveToken(refreshToken: String){
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = refreshToken
        }
    }

    suspend fun clearToken(){
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    val tokenFlow: Flow<String?> =dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    companion object{
        private val TOKEN_KEY = stringPreferencesKey("refreshToken")

        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(context: Context): UserPreferences {
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: UserPreferences(context)
            }.also { INSTANCE = it }
        }
    }
}