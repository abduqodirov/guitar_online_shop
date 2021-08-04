package com.abduqodirov.guitaronlineshop.data.model

import com.squareup.moshi.Json

data class UserDTO(

    @field:Json(name = "id")
    val id: String,

    @field:Json(name = "firstName")
    val firstName: String,

    @field:Json(name = "lastName")
    val lastName: String,

    @field:Json(name = "email")
    val email: String,

    @field:Json(name = "picture")
    val avatarUrl: String,

    @field:Json(name = "isAdmin")
    val isAdmin: Boolean? = null
)
