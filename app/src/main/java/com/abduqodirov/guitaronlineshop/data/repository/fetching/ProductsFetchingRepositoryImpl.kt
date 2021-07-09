package com.abduqodirov.guitaronlineshop.data.repository.fetching

import androidx.lifecycle.MutableLiveData
import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.network.IRemoteDataSource
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

    // var products = MutableLiveData<Response<List<FetchingProduct>>>()
    var productById = MutableLiveData<Response<FetchingProduct>>()

    private val repoJob = Job()
    private val repoScope = CoroutineScope(Dispatchers.Main + repoJob)

    override fun fetchProducts(): Flow<List<FetchingProduct>> = flow {

        // repoScope.launch {
        //
        //     withContext(Dispatchers.IO) {

        // emit(Response.Loading)

        // products.postValue(Response.Loading)

        // try {
        // products.postValue(
        //     Response.Success(remoteDataSource.fetchProducts())
        // )
        emit(remoteDataSource.fetchProducts())
        // } catch (e: Exception) {
        //
        //     // products.postValue(
        //     //     Response.Failure(
        //     //         errorMessage = e.localizedMessage ?: "Failed to load"
        //     //     )
        //     // )
        //     emit(Response.Failure(e.localizedMessage))
        //     e.printStackTrace()
        // }
        //     }
        // }
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
}
