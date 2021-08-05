package com.abduqodirov.guitaronlineshop.data.repository.submitting

import com.abduqodirov.guitaronlineshop.data.mapper.mapSubmittingProduct
import com.abduqodirov.guitaronlineshop.data.model.FetchingProductDTO
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.network.RemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.imageuploader.ImageUploader
import com.abduqodirov.guitaronlineshop.di.scopes.FragmentScope
import com.abduqodirov.guitaronlineshop.view.model.ProductForSendingScreen
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
            emit(Response.Loading)
            val urlsOfUploadedImages = arrayListOf("")

            sendingProduct.photos.forEach {
                val uploadedImage = imageUploader.uploadImage(it, sendingProduct.name)
                urlsOfUploadedImages.add(uploadedImage!!)
            }

            val productWithUploadedImages =
                mapSubmittingProduct(sendingProduct, urlsOfUploadedImages)

            emit(remoteDataSource.submitProduct(productWithUploadedImages))
        }
}
