package com.example.myapplication.src.authusers.domain

interface AuthUserRepository {
    suspend fun login(email: String, password: String): String
    suspend fun getUserInfo(): AuthUser
}