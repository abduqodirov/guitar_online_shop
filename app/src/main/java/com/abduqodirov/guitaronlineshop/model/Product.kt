package com.abduqodirov.guitaronlineshop.model

import com.squareup.moshi.Json

data class Product(

    @field:Json(name = "id")
    val id: String,

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

    //TODO comment parse qilolmayapti
//    @field:Json(name = "comments")
//    val comments: List<Comment>
)
