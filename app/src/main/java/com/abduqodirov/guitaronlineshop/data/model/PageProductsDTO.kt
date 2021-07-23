package com.abduqodirov.guitaronlineshop.data.model

import com.squareup.moshi.Json

data class PageProductsDTO(

    @field:Json(name = "page")
    val page: Int,

    @field:Json(name = "limit")
    val limit: Int,

    @field:Json(name = "data")
    val products: List<FetchingProductDTO>,

    @field:Json(name = "totalCount")
    val totalCount: Long,

    @field:Json(name = "max")
    val max: Double?,

    @field:Json(name = "min")
    val min: Double?,
)
