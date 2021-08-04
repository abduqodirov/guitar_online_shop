package com.abduqodirov.guitaronlineshop.data.network

import com.abduqodirov.guitaronlineshop.data.model.FetchingProductDTO
import com.abduqodirov.guitaronlineshop.data.model.PageProductsDTO
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.model.SendingProductWithUploadedImagesDTO
import com.abduqodirov.guitaronlineshop.data.model.TokenUserDTO
import com.abduqodirov.guitaronlineshop.view.model.SortingFilteringFields

interface RemoteDataSource {

    suspend fun fetchProducts(): List<FetchingProductDTO>

    suspend fun fetchPaginatedProducts(
        pageIndex: Int,
        limit: Int,
        fields: SortingFilteringFields
    ): PageProductsDTO

    suspend fun fetchProductById(id: String): FetchingProductDTO

    suspend fun submitProduct(product: SendingProductWithUploadedImagesDTO): FetchingProductDTO

    suspend fun loginWithEmail(email: String, password: String): Response<TokenUserDTO>

    suspend fun signUpWithEmail(email: String, password: String): TokenUserDTO
}
