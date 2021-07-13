package com.abduqodirov.guitaronlineshop.data.network.retrofit

import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.data.model.PageProducts
import com.abduqodirov.guitaronlineshop.data.model.SendingProductWithUploadedImages
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ShopService {

    @GET("/products")
    suspend fun fetchProducts(): List<FetchingProduct>

    // TODO hardcoded highPrice. Backend engineer should create another endpoint for pagination without filters.
    @GET("/products/search?nameFilter=&lowPrice=0&highPrice=9999999")
    suspend fun fetchPaginatedProducts(
        @Query("page") pageIndex: Int,
        @Query("limit") limit: Int,
    ): PageProducts

    @GET("/products/{id}")
    suspend fun fetchProductById(@Path("id") id: String): FetchingProduct

    @POST("/products")
    suspend fun submitProduct(@Body product: SendingProductWithUploadedImages): FetchingProduct
}
