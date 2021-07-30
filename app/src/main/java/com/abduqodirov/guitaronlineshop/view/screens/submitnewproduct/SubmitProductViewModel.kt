package com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.data.model.FetchingProductDTO
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.repository.submitting.SubmitProductRepository
import com.abduqodirov.guitaronlineshop.view.model.ProductForSendingScreen
import com.abduqodirov.guitaronlineshop.view.model.UploadingImage
import com.abduqodirov.guitaronlineshop.view.model.Validation
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

private const val MINIMUM_DESC_LENGTH = 10
private const val MAXIMUM_DESC_LENGTH = 600

class SubmitProductViewModel @Inject constructor(
    private val submitRepo: SubmitProductRepository
) : ViewModel() {

    private val _formInputsValidation = MutableLiveData(
        arrayOf(
            Validation(EDITTEXT_NAME_POSITION, validator = ::nameValidator),
            Validation(EDITTEXT_PRICE_POSITION, validator = ::priceValidator),
            Validation(EDITTEXT_DESC_POSITION, validator = ::descriptionValidator),
        )
    )
    val formInputsValidation: LiveData<Array<Validation>> get() = _formInputsValidation

    private val _sentProduct = MutableLiveData<Response<FetchingProductDTO>>()
    val sentProduct: LiveData<Response<FetchingProductDTO>> get() = _sentProduct

    private var addImageCount = 0

    lateinit var currentPhotoPath: String
    var currentFile: File? = null

    private val _submittingImages = MutableLiveData<ArrayList<UploadingImage>>(arrayListOf())
    val submittingImages: LiveData<ArrayList<UploadingImage>> = _submittingImages

    fun sendProduct(product: ProductForSendingScreen) {
        viewModelScope.launch {
            submitRepo.sendProduct(product)
                .collect {
                    _sentProduct.postValue(it)
                }
        }
    }

    fun addImage(thumbnailBitmap: Bitmap) {
        val oldImages = submittingImages.value
        val newImages = arrayListOf<UploadingImage>()
        newImages.addAll(oldImages!!)

        newImages.add(
            UploadingImage(
                id = addImageCount,
                thumbnailBitmap = thumbnailBitmap,
                path = currentPhotoPath,
            )
        )

        _submittingImages.value = newImages
        addImageCount++

        // Clearing path before adding next pictures.
        currentPhotoPath = ""
    }

    fun removeImage(id: Int) {
        val oldImages = submittingImages.value
        val newImages = arrayListOf<UploadingImage>()

        oldImages?.forEach { image ->
            if (image.id != id) {
                newImages.add(image)
            }
        }

        _submittingImages.value = newImages
    }

    fun clearStorage() {
        submittingImages.value?.forEach {
            val deletingFile = File(it.path)
            deletingFile.delete()
        }
    }

    fun validateEditText(
        position: Int,
        text: String
    ) {
        val oldValidation = formInputsValidation.value
        val validatorFunction = oldValidation?.get(position)?.validator
        val result = validatorFunction?.invoke(text)

        val validationField = oldValidation?.get(position)

        if (validationField != null) {

            validationField.errorResId = result
            oldValidation[position] = validationField

            _formInputsValidation.value = oldValidation
        }
    }

    private fun nameValidator(name: String): Int? {
        if (name.isEmpty()) {
            return R.string.validation_empty_field
        }

        return null
    }

    private fun priceValidator(text: String): Int? {

        if (text.isEmpty()) {
            return R.string.validation_empty_field
        }

        val price = text.toDoubleOrNull() ?: return R.string.validation_invalid_value

        // TODO add limit to tremendous price
        if (price <= 0) {
            return R.string.validation_price_above_zero
        }
        return null
    }

    private fun descriptionValidator(desc: String): Int? {
        if (desc.isEmpty()) {
            return R.string.validation_empty_field
        }
        if (desc.length < MINIMUM_DESC_LENGTH) {
            return R.string.validation_desc_min
        }
        if (desc.length > MAXIMUM_DESC_LENGTH) {
            return R.string.validation_desc_max
        }
        return null
    }
}
