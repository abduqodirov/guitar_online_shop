package com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abduqodirov.guitaronlineshop.data.repository.submitting.SubmitProductRepository
import com.abduqodirov.guitaronlineshop.data.repository.submitting.SubmitProductRepositoryImpl
import com.abduqodirov.guitaronlineshop.view.model.ProductForSendingScreen
import javax.inject.Inject

private const val MINIMUM_DESC_LENGTH = 10

class SubmitProductViewModel @Inject constructor(
    private val submitRepo: SubmitProductRepository
) : ViewModel() {

    val formInputsValidationLive = MutableLiveData(arrayOf(false, false, false))

    val sentProduct = (submitRepo as SubmitProductRepositoryImpl).sentProduct

    private val validators = arrayOf(::isValidName, ::isValidPrice, ::isValidDesc)

    fun sendProduct(product: ProductForSendingScreen) {
        submitRepo.sendProduct(product)
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
