package com.abduqodirov.guitaronlineshop.data.model

import com.squareup.moshi.Json

data class ErrorMessageDTO(

    @field:Json(name = "statusCode")
    val statusCode: Int,

    @field:Json(name = "message")
    val message: String,
)
