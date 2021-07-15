package com.abduqodirov.guitaronlineshop.data.repository.fetching

import androidx.paging.PagingData
import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import kotlinx.coroutines.flow.Flow

interface ProductsFetchingRepository {

    fun fetchPaginatedProducts(): Flow<PagingData<FetchingProduct>>

    fun fetchProductById(id: String)
}
