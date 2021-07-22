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

    private var _productLive = MutableLiveData<Response<FetchingProduct>>()
    val productLive get() = _productLive

    fun refreshProduct(id: String) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                _productLive.postValue(Response.loading(null))

                try {
                    _productLive.postValue(Response.success(ShopApi.shopService.fetchProductById(id)))

                } catch (e: Exception) {
                    _productLive.postValue(Response.error(null, e.localizedMessage))
                }
            }

        }

    }

}