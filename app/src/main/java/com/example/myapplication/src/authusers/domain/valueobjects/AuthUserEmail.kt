package com.example.myapplication.src.authusers.domain.valueobjects

import com.example.myapplication.src.shared.domain.exceptions.InvalidArgumentException

@JvmInline
value class AuthUserEmail(val value: String) {
    init {
        validate()
    }

    private fun validate() {
        if (value.length < 3) throw InvalidArgumentException("email must have 3 characters or more")
        if (value.length > 40) throw InvalidArgumentException("email cannot have more than 40 characters")
        if (!value.contains("@")) throw InvalidArgumentException("email must contain an @")
    }
}