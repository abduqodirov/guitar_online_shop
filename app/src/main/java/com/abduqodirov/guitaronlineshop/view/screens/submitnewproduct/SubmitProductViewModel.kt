package com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abduqodirov.guitaronlineshop.data.repository.submitting.SubmitProductRepository
import com.abduqodirov.guitaronlineshop.data.repository.submitting.SubmitProductRepositoryImpl
import com.abduqodirov.guitaronlineshop.view.model.ProductForSendingScreen
import com.abduqodirov.guitaronlineshop.view.model.UploadingImage
import java.io.File
import javax.inject.Inject

private const val MINIMUM_DESC_LENGTH = 10

class SubmitProductViewModel @Inject constructor(
    private val submitRepo: SubmitProductRepository
) : ViewModel() {

    val formInputsValidationLive = MutableLiveData(arrayOf(false, false, false))

    val sentProduct = (submitRepo as SubmitProductRepositoryImpl).sentProduct

    var addImageCount = 0

    lateinit var currentPhotoPath: String
    var currentFile: File? = null

    private val _submittingImages = MutableLiveData<ArrayList<UploadingImage>>(arrayListOf())
    val submittingImages: LiveData<ArrayList<UploadingImage>> = _submittingImages

    private val validators = arrayOf(::isValidName, ::isValidPrice, ::isValidDesc)

    fun sendProduct(product: ProductForSendingScreen) {
        submitRepo.sendProduct(product)
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

        oldImages?.forEachIndexed { index, image ->
            if (image.id != id) {
                newImages.add(image)
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

    private fun isValidName(name: String) = name.isNotEmpty()

    private fun isValidPrice(text: String): Boolean {
        if (text.isEmpty()) {
            return false
        }
        val price = text.toDouble()

        return price > 0
    }

    private fun isValidDesc(desc: String): Boolean {
        return desc.isNotEmpty() && desc.length > MINIMUM_DESC_LENGTH
    }
}
