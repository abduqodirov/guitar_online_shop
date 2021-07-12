package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.repository.fetching.ProductsFetchingRepository
import com.abduqodirov.guitaronlineshop.view.mapper.mapFetchedProduct
import com.abduqodirov.guitaronlineshop.view.model.ProductForDisplay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsFetchingRepository
) : ViewModel() {

    val products = MutableLiveData<Response<List<ProductForDisplay>>>(Response.Loading)

    init {
        refreshProducts()
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
