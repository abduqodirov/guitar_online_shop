package com.abduqodirov.guitaronlineshop.data.repository.submitting

import androidx.lifecycle.MutableLiveData
import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.data.model.SendingProduct
import com.abduqodirov.guitaronlineshop.data.network.IRemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.Response
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di.FragmentScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@FragmentScope
class SubmitProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: IRemoteDataSource
) : SubmitProductRepository {

    val sentProduct = MutableLiveData<Response<FetchingProduct>>()

    private val job = Job()
    private val submitScope = CoroutineScope(Dispatchers.Main + job)

    override fun sendProduct(sendingProduct: SendingProduct) {
        submitScope.launch {
            withContext(Dispatchers.IO) {

                sentProduct.postValue(Response.Loading)

                try {
                    val resultProduct = remoteDataSource.submitProduct(sendingProduct)
                    sentProduct.postValue(Response.Success(resultProduct))
                } catch (e: Exception) {

                    sentProduct.postValue(Response.Failure(e.localizedMessage ?: "Failed to load"))
                    e.printStackTrace()
                }
            }
        }
    }
}
