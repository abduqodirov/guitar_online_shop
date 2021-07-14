package com.abduqodirov.guitaronlineshop.data.repository.fetching

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.network.IRemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.paging.ProductsPagingSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductsFetchingRepositoryImpl @Inject constructor(
    private val remoteDataSource: IRemoteDataSource
) : ProductsFetchingRepository {

    var productById = MutableLiveData<Response<FetchingProduct>>()

    private val repoJob = Job()
    private val repoScope = CoroutineScope(Dispatchers.Main + repoJob)

    override fun fetchProducts(): Flow<List<FetchingProduct>> = flow {
        emit(remoteDataSource.fetchProducts())
    }

    override fun fetchPaginatedProducts(): Flow<PagingData<FetchingProduct>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { ProductsPagingSource(remoteDataSource) }
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
        const val PAGE_SIZE = 10
    }
}
