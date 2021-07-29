package com.abduqodirov.guitaronlineshop.data.repository.submitting

import com.abduqodirov.guitaronlineshop.data.model.FetchingProductDTO
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.view.model.ProductForSendingScreen
import kotlinx.coroutines.flow.Flow

interface SubmitProductRepository {

    fun sendProduct(sendingProduct: ProductForSendingScreen): Flow<Response<FetchingProductDTO>>
}
