package com.abduqodirov.guitaronlineshop.network

import com.abduqodirov.guitaronlineshop.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.model.Product
import com.abduqodirov.guitaronlineshop.model.SendingProduct
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

const val BASE_URL = "http://192.168.1.103:3000"

interface ShopService {

    @GET("/products")
    suspend fun fetchProducts(): List<FetchingProduct>

    @GET("/products/{id}")
    suspend fun fetchProductById(@Path("id") id: String): FetchingProduct

    @POST("/products")
    suspend fun submitProduct(@Body product: SendingProduct): FetchingProduct

    @POST("/products")
    fun submitProductCallback(@Body product: SendingProduct): Call<FetchingProduct>

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