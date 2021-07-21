package com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abduqodirov.guitaronlineshop.data.repository.submitting.SubmitProductRepository
import com.abduqodirov.guitaronlineshop.data.repository.submitting.SubmitProductRepositoryImpl
import com.abduqodirov.guitaronlineshop.view.model.ProductForSendingScreen
import timber.log.Timber
import javax.inject.Inject

private const val MINIMUM_DESC_LENGTH = 10

class SubmitProductViewModel @Inject constructor(
    private val submitRepo: SubmitProductRepository
) : ViewModel() {

    val formInputsValidationLive = MutableLiveData(arrayOf(false, false, false))

    val sentProduct = (submitRepo as SubmitProductRepositoryImpl).sentProduct

    private val _submittingImages = MutableLiveData<ArrayList<Bitmap>>(arrayListOf())
    val submittingImages: LiveData<ArrayList<Bitmap>> = _submittingImages

    private val validators = arrayOf(::isValidName, ::isValidPrice, ::isValidDesc)

    fun sendProduct(product: ProductForSendingScreen) {
        submitRepo.sendProduct(product)
        // TODO bitmap yo'q bo'lsa error bervorarkan
    }

    fun addImage(bitmap: Bitmap) {
        var oldImages = submittingImages.value
        val newImages = arrayListOf<Bitmap>()

        newImages.addAll(oldImages!!)
        newImages.add(bitmap)
        _submittingImages.value = newImages
    }

    fun removeImage(position: Int) {
        val oldImages = submittingImages.value
        val newImages = arrayListOf<Bitmap>()

        oldImages?.forEachIndexed { index, bitmap ->
            if (index != position) {
                newImages.add(bitmap)
            } else {
                Timber.d("$position tushib qoldi")
            }
        }

        _submittingImages.value = newImages
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
