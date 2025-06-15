package com.example.myapplication.src.authusers.domain.valueobjects

import com.example.myapplication.src.shared.domain.exceptions.InvalidArgumentException

@JvmInline
value class AuthUserPassword(val value: String) {
    init {
        validate()
    }

    private fun validate() {
        if (value.length < 3) throw InvalidArgumentException("password must have 3 characters or more")
        if (value.length > 40) throw InvalidArgumentException("password cannot have more than 40 characters")
    }
}