package com.abduqodirov.guitaronlineshop.data.repository.submitting

import com.abduqodirov.guitaronlineshop.data.mapper.mapSubmittingProduct
import com.abduqodirov.guitaronlineshop.data.model.FetchingProductDTO
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.network.RemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.imageuploader.ImageUploader
import com.abduqodirov.guitaronlineshop.view.model.ProductForSendingScreen
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di.component.FragmentScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@FragmentScope
class SubmitProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val imageUploader: ImageUploader
) : SubmitProductRepository {

    override fun sendProduct(sendingProduct: ProductForSendingScreen): Flow<Response<FetchingProductDTO>> =
        flow {

            val urlsOfUploadedImages = arrayListOf("")

            sendingProduct.photos.forEach {
                val uploadedImage = imageUploader.uploadImage(it, sendingProduct.name)
                urlsOfUploadedImages.add(uploadedImage!!)
            }

            val productWithUploadedImages =
                mapSubmittingProduct(sendingProduct, urlsOfUploadedImages)

            try {
                val resultProduct = remoteDataSource.submitProduct(productWithUploadedImages)
                emit(Response.Success(resultProduct))
            } catch (e: Exception) {
                emit(Response.Failure(e.localizedMessage ?: "Failed to load"))
            }
        }
}
