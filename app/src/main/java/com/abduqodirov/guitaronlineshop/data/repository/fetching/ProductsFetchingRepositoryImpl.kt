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

                products.postValue(Response.loading(null))

                try {
                    products.postValue(
                        Response.success(data = remoteDataSource.fetchProducts())
                    )
                } catch (e: Exception) {

                    products.postValue(Response.error(data = null, message = e.message))
                    e.printStackTrace()
                }
            }
        }
    }

    override fun fetchProductById(id: String) {
        repoScope.launch {
            withContext(Dispatchers.IO) {

                productById.postValue(Response.loading(null))

                try {
                    productById.postValue(Response.success(remoteDataSource.fetchProductById(id)))
                } catch (e: Exception) {
                    productById.postValue(Response.error(null, e.localizedMessage))
                }
            }
        }
    }
}
