package com.abduqodirov.guitaronlineshop.data.repository.submitting

import com.abduqodirov.guitaronlineshop.data.model.SendingProduct

interface SubmitProductRepository {

    fun sendProduct(sendingProduct: SendingProduct)
}
