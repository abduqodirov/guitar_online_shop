package com.abduqodirov.guitaronlineshop.data.repository.fetching

import androidx.paging.PagingData
import com.abduqodirov.guitaronlineshop.data.model.FetchingProductDTO
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.view.model.SortingFilteringFields
import kotlinx.coroutines.flow.Flow

interface ProductsFetchingRepository {

    fun fetchPaginatedProducts(fields: SortingFilteringFields): Flow<PagingData<FetchingProductDTO>>

    fun fetchProductById(id: String): Flow<Response.Success<FetchingProductDTO>>
}
