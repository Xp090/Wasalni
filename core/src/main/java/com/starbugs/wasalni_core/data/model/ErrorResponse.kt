package com.starbugs.wasalni_core.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "statusCode")
    val statusCode: Int = 0,
    @Json(name = "message")
    val message: String = "",
    @Json(name = "error")
    val error: String = ""
)
