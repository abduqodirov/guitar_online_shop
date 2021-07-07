package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist

import androidx.lifecycle.ViewModel
import com.abduqodirov.guitaronlineshop.data.repository.fetching.ProductsFetchingRepository
import com.abduqodirov.guitaronlineshop.data.repository.fetching.ProductsFetchingRepositoryImpl
import javax.inject.Inject

class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsFetchingRepository
) : ViewModel() {

    val products = (productsRepository as ProductsFetchingRepositoryImpl).products

    fun refreshProducts() {
        productsRepository.fetchProducts()
    }
}
