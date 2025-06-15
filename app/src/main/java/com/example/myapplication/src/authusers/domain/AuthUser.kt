package com.example.myapplication.src.authusers.domain

import com.example.myapplication.src.authusers.domain.valueobjects.AuthUserEmail
import com.example.myapplication.src.authusers.domain.valueobjects.AuthUserName
import com.example.myapplication.src.authusers.domain.valueobjects.AuthUserPassword

data class AuthUser(
    val id: Int,
    val name: AuthUserName,
    val email: AuthUserEmail,
    val password: AuthUserPassword? = null
){
    override fun toString(): String {
        return "AuthUser(id=$id, name=${name.value}, email=${email.value}, password=${password?.value})"
    }
}