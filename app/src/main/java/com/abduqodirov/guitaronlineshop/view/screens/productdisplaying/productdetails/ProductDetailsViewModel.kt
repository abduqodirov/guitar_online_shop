package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.abduqodirov.guitaronlineshop.data.model.FetchingProductDTO
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.repository.fetching.ProductsFetchingRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductDetailsViewModel @Inject constructor(
    private val productsRepository: ProductsFetchingRepository
) : ViewModel() {

    val product = MutableLiveData<Response<FetchingProductDTO>>()

    fun refreshProduct(id: String) {
        viewModelScope.launch {
            product.value = Response.Loading
            try {
                productsRepository.fetchProductById(id)
                    .collect {
                        product.postValue(it)
                    }
                productsRepository.fetchProductById(id).asLiveData()
            } catch (e: Exception) {
                product.value = Response.Failure(e.localizedMessage ?: "Failed to load")
                e.printStackTrace()
            }
        }
    }
}
