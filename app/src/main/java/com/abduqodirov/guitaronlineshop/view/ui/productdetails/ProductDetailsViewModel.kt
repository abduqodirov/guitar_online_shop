package com.abduqodirov.guitaronlineshop.view.ui.productdetails

import androidx.lifecycle.ViewModel
import com.abduqodirov.guitaronlineshop.data.repository.ProductsRepository
import com.abduqodirov.guitaronlineshop.data.repository.ProductsRepositoryImpl
import javax.inject.Inject
import javax.inject.Named

@Named("detail")
class ProductDetailsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
) : ViewModel() {

    val product = (productsRepository as ProductsRepositoryImpl).productById

    fun refreshProduct(id: String) {
        productsRepository.fetchProductById(id)
    }
}
