package com.starbugs.wasalni_core.data.model
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "token")
    val token: String,
    @Json(name = "user")
    val user: User
)

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "id")
    val id: String,
    @Json(name = "updatedAt")
    val updatedAt: String
)