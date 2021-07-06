package com.abduqodirov.guitaronlineshop.data.network

import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.data.model.SendingProduct

// TODO fetch by idga shunaqa interface yo'q ekan.
// Interfacelarni impllarni folderlash cleanlash
interface IRemoteDataSource {

    suspend fun fetchProducts(): List<FetchingProduct>

    suspend fun fetchProductById(id: String): FetchingProduct

    suspend fun submitProduct(product: SendingProduct): FetchingProduct
}
