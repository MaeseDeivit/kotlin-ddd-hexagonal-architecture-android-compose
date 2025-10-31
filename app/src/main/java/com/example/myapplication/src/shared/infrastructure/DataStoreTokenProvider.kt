package com.example.myapplication.src.shared.infrastructure

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.src.shared.domain.TokenProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.dataStore by preferencesDataStore("auth_prefs")

class DataStoreTokenProvider(private val context: Context) : TokenProvider {

    private val KEY_TOKEN = stringPreferencesKey("auth_token")

    override fun setToken(token: String) {
        runBlocking {
            context.dataStore.edit { prefs ->
                prefs[KEY_TOKEN] = token
            }
        }
    }

    override fun getToken(): String? = runBlocking {
        val prefs = context.dataStore.data.first()
        prefs[KEY_TOKEN]
    }

    override fun clearToken() {
        runBlocking {
            context.dataStore.edit { prefs ->
                prefs.remove(KEY_TOKEN)
            }
        }
    }
}