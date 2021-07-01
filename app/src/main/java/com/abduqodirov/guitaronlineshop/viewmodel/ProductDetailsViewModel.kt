package com.abduqodirov.guitaronlineshop.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abduqodirov.guitaronlineshop.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.network.Response
import com.abduqodirov.guitaronlineshop.network.ShopApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailsViewModel : ViewModel() {

    private var _product = MutableLiveData<Response<FetchingProduct>>()
    val product get() = _product

    fun refreshProduct(id: String) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                _product.postValue(Response.loading(null))

                try {
                    _product.postValue(Response.success(ShopApi.shopService.fetchProductById(id)))
                } catch (e: Exception) {
                    _product.postValue(Response.error(null, e.localizedMessage))
                }
            }
        }
    }
}
