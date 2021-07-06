package com.abduqodirov.guitaronlineshop.data.repository.fetching

import androidx.lifecycle.MutableLiveData
import com.abduqodirov.guitaronlineshop.data.model.Product
import com.abduqodirov.guitaronlineshop.data.network.IRemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductsFetchingRepositoryImpl @Inject constructor(
    private val remoteDataSource: IRemoteDataSource
) : ProductsFetchingRepository {

    var products = MutableLiveData<Response<List<Product>>>()
    var productById = MutableLiveData<Response<Product>>()

    private val repoJob = Job()
    private val repoScope = CoroutineScope(Dispatchers.Main + repoJob)

    override fun fetchProducts() {

        repoScope.launch {

            withContext(Dispatchers.IO) {

                products.postValue(Response.Loading)

                try {
                    products.postValue(
                        Response.Success(remoteDataSource.fetchProducts())
                    )
                } catch (e: Exception) {

                    products.postValue(
                        Response.Failure(
                            errorMessage = e.localizedMessage ?: "Failed to load"
                        )
                    )
                    e.printStackTrace()
                }
            }
        }
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
