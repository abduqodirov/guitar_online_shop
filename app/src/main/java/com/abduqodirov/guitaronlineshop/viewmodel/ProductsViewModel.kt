package com.abduqodirov.guitaronlineshop.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abduqodirov.guitaronlineshop.model.Product
import com.abduqodirov.guitaronlineshop.network.Response
import com.abduqodirov.guitaronlineshop.network.ShopApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsViewModel : ViewModel() {

    private var _products = MutableLiveData<Response<List<Product>>>()
    var products: LiveData<Response<List<Product>>> = _products

    fun refreshProducts() {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                _products.postValue(Response.loading(null))

                try {
                    _products.postValue(
                        Response.success(data = ShopApi.shopService.fetchProducts())
                    )
                } catch (e: Exception) {

                    _products.postValue(Response.error(data = null, message = e.message))
                    e.printStackTrace()
                }
            }
        }
    }
}
