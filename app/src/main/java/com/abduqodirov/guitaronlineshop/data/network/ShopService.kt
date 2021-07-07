package com.abduqodirov.guitaronlineshop.data.network

import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.data.model.SendingProduct
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ShopService {

    @GET("/products")
    suspend fun fetchProducts(): List<FetchingProduct>

    @GET("/products/{id}")
    suspend fun fetchProductById(@Path("id") id: String): FetchingProduct

    @POST("/products")
    suspend fun submitProduct(@Body product: SendingProduct): FetchingProduct
}