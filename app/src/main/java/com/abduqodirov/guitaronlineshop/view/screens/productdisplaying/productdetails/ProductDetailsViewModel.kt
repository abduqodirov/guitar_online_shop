package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productdetails

import androidx.lifecycle.ViewModel
import com.abduqodirov.guitaronlineshop.data.repository.fetching.ProductsFetchingRepository
import com.abduqodirov.guitaronlineshop.data.repository.fetching.ProductsFetchingRepositoryImpl
import javax.inject.Inject

class ProductDetailsViewModel @Inject constructor(
    private val productsRepository: ProductsFetchingRepository
) : ViewModel() {

    // TODO Should be migrated to Kotlin Flow in order getting rid of cast
    val product = (productsRepository as ProductsFetchingRepositoryImpl).productById

    fun refreshProduct(id: String) {
        productsRepository.fetchProductById(id)
    }
}
