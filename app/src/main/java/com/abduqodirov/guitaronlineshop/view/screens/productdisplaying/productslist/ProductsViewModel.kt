package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.repository.fetching.ProductsFetchingRepository
import com.abduqodirov.guitaronlineshop.view.mapper.mapFetchedProduct
import com.abduqodirov.guitaronlineshop.view.model.ProductForDisplay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsFetchingRepository
) : ViewModel() {

    val products = MutableLiveData<Response<List<ProductForDisplay>>>(Response.Loading)

    private var paginatedProducts: Flow<PagingData<ProductForDisplay>>? = null

    // init {
    //     // refreshProducts()
    //     fetchProducts()
    // }

    fun fetchProducts(): Flow<PagingData<ProductForDisplay>> {

        val newResult: Flow<PagingData<ProductForDisplay>> =
            productsRepository.fetchPaginatedProducts().map { value: PagingData<FetchingProduct> ->
                Log.d("vmm", "fetchProducts: ishga tushdi tashqi map")

                value.map {
                    Log.d("vmm", "fetchProducts: ishga tushdi ichki map")
                    mapFetchedProduct(it)
                }
            }.cachedIn(viewModelScope)

        paginatedProducts = newResult

        return newResult
    }

    fun refreshProducts() {

        viewModelScope.launch {

            productsRepository.fetchProducts()
                .catch { exception ->
                    products.value = Response.Failure(exception.localizedMessage)
                }
                .onEach { fetchingProducts ->
                    products.value = Response.Success(
                        data = fetchingProducts.map { product ->
                            mapFetchedProduct(product)
                        }
                    )
                }
                .collect {
                }
        }
    }
}
