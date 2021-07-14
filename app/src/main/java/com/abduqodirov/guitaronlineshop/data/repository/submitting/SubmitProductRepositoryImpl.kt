package com.abduqodirov.guitaronlineshop.data.repository.submitting

import androidx.lifecycle.MutableLiveData
import com.abduqodirov.guitaronlineshop.data.mapper.mapSubmittingProduct
import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.network.IRemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.imageuploader.ImageUploader
import com.abduqodirov.guitaronlineshop.view.model.ProductForSendingScreen
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di.component.FragmentScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@FragmentScope
class SubmitProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: IRemoteDataSource,
    private val imageUploader: ImageUploader
) : SubmitProductRepository {

    val sentProduct = MutableLiveData<Response<FetchingProduct>>()

    private val job = Job()

    private val submitScope = CoroutineScope(Dispatchers.Main + job)

    override fun sendProduct(sendingProduct: ProductForSendingScreen) {
        submitScope.launch {

            withContext(Dispatchers.IO) {

                sentProduct.postValue(Response.Loading)

                try {
                    val urlsOfUploadedImages = arrayListOf<String>("")

                    // TODO uncomment commented lines
                    // sendingProduct.photos.forEach {
                    //     urlsOfUploadedImages.add(imageUploader.uploadImage(it))
                    // }

                    val productWithUploadedImages =
                        mapSubmittingProduct(sendingProduct, urlsOfUploadedImages)

                    val resultProduct = remoteDataSource.submitProduct(productWithUploadedImages)

                    sentProduct.postValue(Response.Success(resultProduct))
                } catch (e: Exception) {

                    sentProduct.postValue(Response.Failure(e.localizedMessage ?: "Failed to load"))
                    e.printStackTrace()
                }
            }
        }
    }
}
