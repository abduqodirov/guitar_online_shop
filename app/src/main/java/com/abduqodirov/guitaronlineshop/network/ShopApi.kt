package com.abduqodirov.guitaronlineshop.network

import com.abduqodirov.guitaronlineshop.model.Product
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

const val BASE_URL = "http://192.168.0.101:3000"

interface ShopService {

    @GET("/products")
    suspend fun fetchProducts(): List<Product>

}

val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

object ShopApi {
    val shopService: ShopService by lazy {
        retrofit.create(ShopService::class.java)
    }
}