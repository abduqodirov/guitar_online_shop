package com.abduqodirov.guitaronlineshop.data.network.retrofit

import com.abduqodirov.guitaronlineshop.data.model.FetchingProductDTO
import com.abduqodirov.guitaronlineshop.data.model.PageProductsDTO
import com.abduqodirov.guitaronlineshop.data.model.SendingProductWithUploadedImagesDTO
import com.abduqodirov.guitaronlineshop.data.model.TokenUserDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ShopService {

    @GET("products")
    suspend fun fetchProducts(): List<FetchingProductDTO>

    @GET("products/search")
    suspend fun fetchPaginatedProducts(
        @Query("page") pageIndex: Int,
        @Query("limit") limit: Int,
        @Query("sortedBy") sortedBy: String,
        @Query("nameFilter") nameFilter: String,
        @Query("lowPrice") lowPrice: Int,
        @Query("highPrice") highPrice: Int,
        @Query("orderOfSort") orderOfSort: String
    ): PageProductsDTO

    @GET("products/{id}")
    suspend fun fetchProductById(@Path("id") id: String): FetchingProductDTO

    @POST("products")
    suspend fun submitProduct(@Body product: SendingProductWithUploadedImagesDTO): FetchingProductDTO

    @POST("login")
    suspend fun loginWithEmail(@Body emailAndPassword: Map<String, String>): TokenUserDTO

    @POST("auth_reg")
    suspend fun signUpWithEmail(@Body emailAndPassword: Map<String, String>): TokenUserDTO
}
