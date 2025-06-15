package com.example.myapplication.ui


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.src.authusers.domain.AuthUser
import com.example.myapplication.stores.AuthUserStore
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    fun login(user: AuthUser) {
        viewModelScope.launch {
            AuthUserStore.setAuthUser(getApplication(), user)
        }
    }

    fun logout() {
        viewModelScope.launch {
            AuthUserStore.logout(getApplication())
        }
    }

    fun loadUser(): AuthUser? {
        return AuthUserStore.loadAuthUser(getApplication())
    }
}