package com.abduqodirov.guitaronlineshop.data.network

import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.data.model.PageProducts
import com.abduqodirov.guitaronlineshop.data.model.SendingProductWithUploadedImages

interface IRemoteDataSource {

    suspend fun fetchProducts(): List<FetchingProduct>

    suspend fun fetchPaginatedProducts(pageIndex: Int, limit: Int): PageProducts

    suspend fun fetchProductById(id: String): FetchingProduct

    suspend fun submitProduct(product: SendingProductWithUploadedImages): FetchingProduct
}
