package com.abduqodirov.guitaronlineshop.data.model

data class TokenUserDTO(
    val accessToken: String,
    val refreshToken: String,
    val userDTO: UserDTO
)
