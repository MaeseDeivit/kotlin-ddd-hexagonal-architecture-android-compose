package com.example.myapplication.stores

import android.content.Context
import android.util.Log
import com.example.myapplication.src.authusers.domain.AuthUser
import com.example.myapplication.src.authusers.domain.valueobjects.AuthUserEmail
import com.example.myapplication.src.authusers.domain.valueobjects.AuthUserName
import com.example.myapplication.src.authusers.domain.valueobjects.AuthUserPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject

object AuthUserStore {
    private val _authUserFlow = MutableStateFlow<AuthUser?>(null)
    val authUserFlow = _authUserFlow.asStateFlow()

    private const val PREFS_NAME = "auth_store"
    private const val KEY_AUTH_USER = "auth_user"

    fun setAuthUser(context: Context, user: AuthUser) {
        _authUserFlow.value = user

        val json = JSONObject().apply {
            put("id", user.id)
            put("name", user.name.value)
            put("email", user.email.value)
            put("password", user.password?.value)
        }.toString()
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_AUTH_USER, json).apply()
        Log.d("AuthUserStore", "authUser set to: $user")
    }

    fun logout(context: Context) {
        _authUserFlow.value = null
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_AUTH_USER).apply()
        Log.d("AuthUserStore", "authUser removed")
    }

    fun loadAuthUser(context: Context): AuthUser? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_AUTH_USER, null)
        val user = json?.let {
            val jsonObject = JSONObject(it)
            AuthUser(
                id = jsonObject.getInt("id"),
                name = AuthUserName(jsonObject.getString("name")),
                email = AuthUserEmail(jsonObject.getString("email")),
                password = jsonObject.optString("password").let { password ->
                    if (password.isNotEmpty()) AuthUserPassword(password) else null
                }
            )
        }
        _authUserFlow.value = user
        Log.d("AuthUserStore", "Loaded authUser: $user")
        return user
    }
}