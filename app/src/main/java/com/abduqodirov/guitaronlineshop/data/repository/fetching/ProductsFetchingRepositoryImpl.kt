package com.abduqodirov.guitaronlineshop.data.repository.fetching

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.abduqodirov.guitaronlineshop.data.model.FetchingProductDTO
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.network.RemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.paging.ProductsPagingSource
import com.abduqodirov.guitaronlineshop.view.model.SortingFilteringFields
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductsFetchingRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : ProductsFetchingRepository {

    var productById = MutableLiveData<Response<FetchingProductDTO>>()

    private val repoJob = Job()
    private val repoScope = CoroutineScope(Dispatchers.Main + repoJob)

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

    override fun fetchProductById(id: String) {
        repoScope.launch {
            withContext(Dispatchers.IO) {

                productById.postValue(Response.Loading)

                try {
                    productById.postValue(Response.Success(remoteDataSource.fetchProductById(id)))
                } catch (e: Exception) {
                    productById.postValue(Response.Failure(e.localizedMessage ?: "Failed to load"))
                }
            }
        }
    }

    companion object {
        const val PAGE_SIZE = 30
    }
}
