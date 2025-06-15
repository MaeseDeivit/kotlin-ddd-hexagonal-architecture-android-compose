package com.example.myapplication.src.authusers.application

import android.util.Log
import com.example.myapplication.src.authusers.domain.AuthUser
import com.example.myapplication.src.authusers.domain.AuthUserRepository
import kotlinx.coroutines.delay

class AuthUserServices(private val authUserRepository: AuthUserRepository) {

    suspend fun login(email: String, password: String): AuthUser {
        var token= authUserRepository.login(email, password)
        Log.d("Token", token)

        delay(2000)
        var authUser= getUserInfo()
        Log.d("authUser", authUser.toString())
return authUser
    }

    suspend fun getUserInfo(): AuthUser {
        return authUserRepository.getUserInfo()
    }

}