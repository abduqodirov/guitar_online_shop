package com.abduqodirov.guitaronlineshop.data.network

import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.data.model.PageProducts
import com.abduqodirov.guitaronlineshop.data.model.SendingProductWithUploadedImages
import com.abduqodirov.guitaronlineshop.view.model.SortingFilteringFields

interface IRemoteDataSource {

    suspend fun fetchProducts(): List<FetchingProduct>

    suspend fun fetchPaginatedProducts(
        pageIndex: Int,
        limit: Int,
        fields: SortingFilteringFields
    ): PageProducts

    suspend fun fetchProductById(id: String): FetchingProduct

    suspend fun submitProduct(product: SendingProductWithUploadedImages): FetchingProduct
}
