package com.abduqodirov.guitaronlineshop.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abduqodirov.guitaronlineshop.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.model.Product
import com.abduqodirov.guitaronlineshop.model.SendingProduct
import com.abduqodirov.guitaronlineshop.network.Response
import com.abduqodirov.guitaronlineshop.network.ShopApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubmitProductViewModel : ViewModel() {

    private var _sendingProduct = MutableLiveData<Response<FetchingProduct>>()
    var sendingProduct: LiveData<Response<FetchingProduct>> = _sendingProduct

    val formInputsValidationLive = MutableLiveData<Array<Boolean>>(arrayOf(false, false, false))

    fun sendProduct(product: SendingProduct) {


        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                _sendingProduct.postValue(Response.loading(null))

                try {
                    val resultProduct = ShopApi.shopService.submitProduct(product)
                    _sendingProduct.postValue(Response.success(resultProduct))
                } catch (e: Exception) {

                    _sendingProduct.postValue(Response.error(null, e.localizedMessage))
                    e.printStackTrace()
                }

            }
        }

    }

}