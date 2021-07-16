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

    @GET("/products/search")
    suspend fun fetchPaginatedProducts(
        @Query("page") pageIndex: Int,
        @Query("limit") limit: Int,
        @Query("sortedBy") sortedBy: String,
        @Query("nameFilter") nameFilter: String,
        @Query("lowPrice") lowPrice: Int,
        @Query("highPrice") highPrice: Int,
        @Query("orderOfSort") orderOfSort: String
    ): PageProducts

    @GET("/products/{id}")
    suspend fun fetchProductById(@Path("id") id: String): FetchingProduct

    @POST("/products")
    suspend fun submitProduct(@Body product: SendingProductWithUploadedImages): FetchingProduct
}
