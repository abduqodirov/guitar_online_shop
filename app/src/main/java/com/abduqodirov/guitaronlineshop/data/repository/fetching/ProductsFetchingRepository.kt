package com.abduqodirov.guitaronlineshop.data.repository.fetching

import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import kotlinx.coroutines.flow.Flow

interface ProductsFetchingRepository {

    fun fetchProducts(): Flow<List<FetchingProduct>>

    fun fetchProductById(id: String)
}
