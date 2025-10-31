package com.example.myapplication.src.shared.infrastructure

import com.example.myapplication.src.shared.domain.TokenProvider

object InMemoryTokenProvider : TokenProvider {
    private var token: String? = null

    override fun getToken() = token

    override fun setToken(token: String) {
        this.token = token
    }

    override fun clearToken() {
        this.token = null
    }
}