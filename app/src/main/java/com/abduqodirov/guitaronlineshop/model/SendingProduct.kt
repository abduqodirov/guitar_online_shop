package com.abduqodirov.guitaronlineshop.model

import com.squareup.moshi.Json

data class SendingProduct(

    @field:Json(name = "name")
    val name: String,

    @field:Json(name = "price")
    val price: Double,

    @field:Json(name = "description")
    val description: String,

    @field:Json(name = "photos")
    val photos: List<String>,

    @field:Json(name = "rating")
    val rating: List<Double>,

    @field:Json(name = "comments")
    val comments: List<String>

): Product()