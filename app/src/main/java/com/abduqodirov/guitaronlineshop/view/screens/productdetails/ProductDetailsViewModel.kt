package com.abduqodirov.guitaronlineshop.view.screens.productdetails

import androidx.lifecycle.ViewModel
import com.abduqodirov.guitaronlineshop.data.repository.fetching.ProductsFetchingRepository
import com.abduqodirov.guitaronlineshop.data.repository.fetching.ProductsFetchingRepositoryImpl
import javax.inject.Inject
import javax.inject.Named

@Named("detail")
class ProductDetailsViewModel @Inject constructor(
    private val productsRepository: ProductsFetchingRepository
) : ViewModel() {

    val product = (productsRepository as ProductsFetchingRepositoryImpl).productById

    fun refreshProduct(id: String) {
        productsRepository.fetchProductById(id)
    }
}
