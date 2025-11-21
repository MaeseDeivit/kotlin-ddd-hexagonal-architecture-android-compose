package com.example.myapplication.src.shared.infrastructure

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.src.shared.domain.TokenProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private val Context.dataStore by preferencesDataStore("auth_prefs")

class DataStoreTokenProvider(private val context: Context) : TokenProvider {

    private val KEY_TOKEN = stringPreferencesKey("auth_token")

    override fun setToken(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { prefs ->
                prefs[KEY_TOKEN] = token
            }
        }
    }

    override fun getToken(): String? {
        return runBlocking {
            try {
                val prefs = context.dataStore.data.first()
                prefs[KEY_TOKEN]
            } catch (e: Exception) {
                Log.e("TokenProvider", "Error getting token", e)
                null
            }
        }
    }
}