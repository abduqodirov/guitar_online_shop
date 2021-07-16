package com.abduqodirov.guitaronlineshop.data.repository.fetching

import androidx.paging.PagingData
import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.view.model.SortingFilteringFields
import kotlinx.coroutines.flow.Flow

interface ProductsFetchingRepository {

    fun fetchPaginatedProducts(fields: SortingFilteringFields): Flow<PagingData<FetchingProduct>>

    fun fetchProductById(id: String)
}
