package com.abduqodirov.guitaronlineshop.data.repository.submitting

import androidx.lifecycle.MutableLiveData
import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.data.model.SendingProduct
import com.abduqodirov.guitaronlineshop.data.network.IRemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SubmitProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: IRemoteDataSource
) : SubmitProductRepository {

    val sentProduct = MutableLiveData<Response<FetchingProduct>>()

    private val job = Job()
    private val submitScope = CoroutineScope(Dispatchers.Main + job)

    override fun sendProduct(sendingProduct: SendingProduct) {
        submitScope.launch {
            withContext(Dispatchers.IO) {

                sentProduct.postValue(Response.loading(null))

                try {
                    val resultProduct = remoteDataSource.submitProduct(sendingProduct)
                    sentProduct.postValue(Response.success(resultProduct))
                } catch (e: Exception) {

                    sentProduct.postValue(Response.error(null, e.localizedMessage))
                    e.printStackTrace()
                }
            }
        }
    }
}
