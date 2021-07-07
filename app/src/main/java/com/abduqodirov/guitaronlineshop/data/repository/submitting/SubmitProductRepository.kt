package com.abduqodirov.guitaronlineshop.data.repository.submitting

import com.abduqodirov.guitaronlineshop.view.model.ProductForSendingScreen

interface SubmitProductRepository {

    fun sendProduct(sendingProduct: ProductForSendingScreen)
}
