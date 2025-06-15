package com.example.myapplication.src.shared.domain.exceptions

import io.ktor.http.*

class InvalidArgumentException(message: String) : GlobalException(
    message, 9001, HttpStatusCode.BadRequest
) {}