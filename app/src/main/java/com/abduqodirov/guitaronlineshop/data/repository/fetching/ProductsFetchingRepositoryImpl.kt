package com.abduqodirov.guitaronlineshop.data.repository.fetching

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.abduqodirov.guitaronlineshop.data.model.FetchingProductDTO
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.network.RemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.paging.ProductsPagingSource
import com.abduqodirov.guitaronlineshop.view.model.SortingFilteringFields
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductsFetchingRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : ProductsFetchingRepository {

    override fun fetchPaginatedProducts(fields: SortingFilteringFields): Flow<PagingData<FetchingProductDTO>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                ProductsPagingSource(
                    dataSource = remoteDataSource,
                    fields = fields
                )
            }
        ).flow
    }

    override fun fetchProductById(id: String) = flow {
        try {
            emit(Response.Success(remoteDataSource.fetchProductById(id)))
        } catch (e: Exception) {
            emit(Response.Failure(e.localizedMessage ?: "Failed to load"))
        }
    }

    companion object {
        const val PAGE_SIZE = 30
    }
}
