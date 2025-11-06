package com.example.myapplication.src.shared.domain

interface TokenProvider {
    fun getToken(): String?
    fun setToken(token: String)
}