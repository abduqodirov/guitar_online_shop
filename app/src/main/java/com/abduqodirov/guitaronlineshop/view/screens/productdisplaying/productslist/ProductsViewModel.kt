package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    // val products = (productsRepository as ProductsFetchingRepositoryImpl).products
    val products = MutableLiveData<List<ProductForDisplay>>()

    fun refreshProducts() {

        viewModelScope.launch {

            productsRepository.fetchProducts()
                .catch {
                    Log.d("vmm", "exception")
                }
                .onEach {
                    products.value = it.map {
                        mapFetchedProduct(it)
                    }
                }
                .collect {
                }
        }
    }
}
