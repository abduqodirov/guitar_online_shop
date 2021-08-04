package com.abduqodirov.guitaronlineshop.data.model

import com.squareup.moshi.Json

data class TokenUserDTO(
    @field:Json(name = "accessToken")
    val accessToken: String,

    @field:Json(name = "refreshToken")
    val refreshToken: String,

    @field:Json(name = "userDTO")
    val userDTO: UserDTO,
)
