package com.example.myapplication.src.shared.domain.exceptions

import io.ktor.http.*

abstract class GlobalException(
    override val message: String, open val errorCode: Int, open val httpStatus: HttpStatusCode
) : Exception(message) {
}