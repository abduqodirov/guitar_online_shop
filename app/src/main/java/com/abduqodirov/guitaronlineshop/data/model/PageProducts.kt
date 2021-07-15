package com.abduqodirov.guitaronlineshop.data.model

import com.squareup.moshi.Json

data class PageProducts(

    @field:Json(name = "page")
    val page: Int,

    @field:Json(name = "limit")
    val limit: Int,

    @field:Json(name = "data")
    val products: List<FetchingProduct>,

    @field:Json(name = "totalCount")
    val totalCount: Long,

    @field:Json(name = "max")
    val max: Float,

    @field:Json(name = "min")
    val min: Float,
)
