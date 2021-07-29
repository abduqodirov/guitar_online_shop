package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    private val _product = MutableLiveData<Response<FetchingProductDTO>>()
    val product: LiveData<Response<FetchingProductDTO>> get() = _product

    fun refreshProduct(id: String) {
        viewModelScope.launch {
            productsRepository.fetchProductById(id)
                .collect {
                    _product.postValue(it)
                }
        }
    }
}
