package com.abduqodirov.guitaronlineshop.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abduqodirov.guitaronlineshop.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.model.SendingProduct
import com.abduqodirov.guitaronlineshop.network.Response
import com.abduqodirov.guitaronlineshop.network.ShopApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val MINIMUM_DESC_LENGTH = 10

class SubmitProductViewModel : ViewModel() {

    val formInputsValidationLive = MutableLiveData<Array<Boolean>>(arrayOf(false, false, false))

    private var _sendingProduct = MutableLiveData<Response<FetchingProduct>>()
    var sendingProduct: LiveData<Response<FetchingProduct>> = _sendingProduct

    private val validators = arrayOf(::isValidName, ::isValidPrice, ::isValidDesc)

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

    fun validateEditText(
        position: Int,
        text: String
    ) {
        val oldValidation = formInputsValidationLive.value
        val result = validators[position](text)
        oldValidation?.set(position, result)
        formInputsValidationLive.value = oldValidation!!
    }

    fun isValidName(name: String) = name.isNotEmpty()

    fun isValidPrice(text: String): Boolean {
        if (text.isEmpty()) {
            return false
        }
        val price = text.toDouble()

        return price > 0
    }

    fun isValidDesc(desc: String): Boolean {
        return desc.isNotEmpty() && desc.length > MINIMUM_DESC_LENGTH
    }
}
